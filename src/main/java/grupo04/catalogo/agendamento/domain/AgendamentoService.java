package grupo04.catalogo.agendamento.domain;

import lombok.RequiredArgsConstructor;
import grupo04.catalogo.agendamento.boundary.AgendamentoRequestDto;
import grupo04.catalogo.agendamento.repository.AgendamentoEntity;
import grupo04.catalogo.agendamento.repository.AgendamentoRepository;
import grupo04.catalogo.catalogoremoto.domain.CatalogoResultado;
import grupo04.catalogo.catalogoremoto.domain.FeriadoService;
import grupo04.catalogo.platform.error.exception.HorariosSobrepostosException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/** Coordena operações de negócio e define os limites das transações. */
@ApplicationScoped
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;

    @Inject
    FeriadoService feriadoService;

    @Inject
    MeterRegistry meterRegistry;

    public List<AgendamentoBo> listar(String nome) {
        meterRegistry.counter("agendamento.consultas", "operacao", "listar").increment();

        List<AgendamentoEntity> entidades = (nome == null || nome.isBlank())
                ? repository.listAll(Sort.by("id"))
                : repository.buscarPorNome(nome);
        return entidades.stream().map(AgendamentoService::toDomain).toList();
    }

    public AgendamentoBo buscar(Long id) {
        meterRegistry.counter("agendamento.consultas", "operacao", "buscar").increment();

        AgendamentoEntity entity = repository.findById(id);
        return entity == null ? null : toDomain(entity);
    }

    @Transactional
    public AgendamentoBo criar(AgendamentoRequestDto request) {
        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            CatalogoResultado catalogoFeriados = feriadoService.getFeriados();

            validarFeriado(request, catalogoFeriados);
            validarSobreposicao(request);

            AgendamentoEntity entity = new AgendamentoEntity(
                    request.nome().trim(),
                    toString(request.dataInicio()),
                    toString(request.horaInicio()),
                    toString(request.dataFim()),
                    toString(request.horaFim()));

            repository.persist(entity);

            meterRegistry.counter("agendamento.criados").increment();

            return toDomain(entity);
        } finally {
            timer.stop(
                    Timer.builder("agendamento.operacao.tempo")
                            .tag("operacao", "criar")
                            .register(meterRegistry));
        }
    }

    private void validarFeriado(AgendamentoRequestDto request, CatalogoResultado catalogo) {
        boolean feriado = catalogo.feriados().stream()
                .anyMatch(f -> f.data().equals(request.dataInicio()));

        if (feriado) {
            meterRegistry.counter("agendamento.rejeitados", "motivo", "FERIADO").increment();

            throw new DiaIndisponivelException(toString(request.dataInicio()));
        }
    }

    private void validarSobreposicao(AgendamentoRequestDto request) {
        validarSobreposicao(null, request);
    }

    private void validarSobreposicao(Long id, AgendamentoRequestDto request) {

        LocalDate inicioData = request.dataInicio();
        LocalTime inicioHora = request.horaInicio();

        LocalDate fimData = request.dataFim();
        LocalTime fimHora = request.horaFim();

        boolean novoPontual = fimData == null && fimHora == null;

        if ((fimData == null) != (fimHora == null)) {
            throw new IllegalArgumentException(
                    "Data e hora de término devem ser informadas juntas.");
        }

        List<AgendamentoEntity> existentes = repository.findAll().list()
                .stream()
                .filter(x -> id == null || !x.id.equals(id))
                .toList();

        if (novoPontual) {
            fimData = inicioData;
            fimHora = inicioHora;
        }

        for (AgendamentoEntity existente : existentes) {

            LocalDate existenteInicioData = parseDate(existente.dataInicio);
            LocalTime existenteInicioHora = parseTime(existente.horaInicio);

            LocalDate existenteFimData = parseDate(existente.dataFim);
            LocalTime existenteFimHora = parseTime(existente.horaFim);

            boolean existentePontual = existenteFimData == null && existenteFimHora == null;

            if (existentePontual) {
                existenteFimData = existenteInicioData;
                existenteFimHora = existenteInicioHora;
            }

            boolean sobreposicao = comparar(inicioData, inicioHora,
                    existenteFimData, existenteFimHora) <= 0
                    &&
                    comparar(existenteInicioData, existenteInicioHora,
                            fimData, fimHora) <= 0;

            if (sobreposicao) {
                meterRegistry.counter("agendamento.rejeitados", "motivo", "SOBREPOSICAO").increment();

                throw new HorariosSobrepostosException();
            }
        }
    }

    private int comparar(
            LocalDate dataA,
            LocalTime horaA,
            LocalDate dataB,
            LocalTime horaB) {

        int comparacaoData = dataA.compareTo(dataB);

        if (comparacaoData != 0) {
            return comparacaoData;
        }

        return horaA.compareTo(horaB);
    }

    @Transactional
    public AgendamentoBo atualizar(Long id, AgendamentoRequestDto request) {
        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            CatalogoResultado catalogoFeriados = feriadoService.getFeriados();

            validarFeriado(request, catalogoFeriados);
            validarSobreposicao(id, request);

            AgendamentoEntity entity = repository.findById(id);
            if (entity == null) {
                return null;
            }
            // Dirty checking: Hibernate atualiza o registro no commit da transação.
            entity.nome = request.nome().trim();
            entity.dataInicio = toString(request.dataInicio());
            entity.horaInicio = toString(request.horaInicio());
            entity.dataFim = toString(request.dataFim());
            entity.horaFim = toString(request.horaFim());

            meterRegistry.counter("agendamento.atualizados").increment();

            return toDomain(entity);
        } finally {
            timer.stop(
                    Timer.builder("agendamento.operacao.tempo")
                            .tag("operacao", "atualizar")
                            .register(meterRegistry));
        }
    }

    @Transactional
    public boolean excluir(Long id) {
        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            boolean excluido = repository.deleteById(id);

            if (excluido) {
                meterRegistry.counter("agendamento.excluidos").increment();
            }

            return excluido;
        } finally {
            timer.stop(
                    Timer.builder("agendamento.operacao.tempo")
                            .tag("operacao", "excluir")
                            .register(meterRegistry));
        }
    }

    private static AgendamentoBo toDomain(AgendamentoEntity entity) {
        LocalDate dataInicio = parseDate(entity.dataInicio);
        LocalTime horaInicio = parseTime(entity.horaInicio);
        LocalDate dataFim = parseDate(entity.dataFim);
        LocalTime horaFim = parseTime(entity.horaFim);

        return new AgendamentoBo(entity.id, entity.nome, dataInicio, horaInicio, dataFim, horaFim);
    }

    private static LocalDate parseDate(String value) {
        try {
            return value != null && !value.isBlank()
                    ? LocalDate.parse(value)
                    : null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static LocalTime parseTime(String value) {
        try {
            return value != null && !value.isBlank()
                    ? LocalTime.parse(value)
                    : null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static String toString(Object value) {
        try {
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}