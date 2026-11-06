package grupo04.catalogo.catalogoremoto.domain;

import grupo04.catalogo.catalogoremoto.integration.estoque.FeriadoGateway;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import grupo04.catalogo.catalogoremoto.integration.estoque.Catalogo;
import grupo04.catalogo.catalogoremoto.domain.CatalogoResultado.FeriadoDisponivel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class FeriadoService {

    private final FeriadoGateway feriadoGateway;

    @Inject
    MeterRegistry meterRegistry;

    public FeriadoService(@RestClient FeriadoGateway feriadoGateway) {
        this.feriadoGateway = feriadoGateway;
    }

    @Retry(maxRetries = 2, delay = 200)
    @Fallback(fallbackMethod = "fallbackFeriados")
    public CatalogoResultado getFeriados() {

        meterRegistry.counter("feriado.consultas", "origem", "API_EXTERNA").increment();

        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            Catalogo catalogoExterno = feriadoGateway.feriados();

            List<FeriadoDisponivel> disponiveis = catalogoExterno.feriados().stream()
                    .map(product -> new FeriadoDisponivel(
                            (long) product.id(),
                            product.nome(),
                            product.data()
                    ))
                    .toList();

            meterRegistry.counter("feriado.consultas.sucesso","origem", "API_EXTERNA").increment();
            meterRegistry.summary("feriado.retorno.quantidade","origem", "API_EXTERNA").record(disponiveis.size());

            return new CatalogoResultado(disponiveis, "API_EXTERNA");
        } catch (Exception e) {
            meterRegistry.counter("feriado.consultas.erro","origem", "API_EXTERNA").increment();

            throw e;
        } finally {
            timer.stop(
                Timer.builder("feriado.consultas.tempo")
                        .tag("origem", "API_EXTERNA")
                        .register(meterRegistry)
            );
        }
    }

    public CatalogoResultado fallbackFeriados() {
        return new CatalogoResultado(List.of(), "FALLBACK");
    }
}
