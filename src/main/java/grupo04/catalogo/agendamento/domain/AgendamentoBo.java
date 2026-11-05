package grupo04.catalogo.agendamento.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoBo(Long id, String nome, LocalDate dataInicio, LocalTime horaInicio, LocalDate dataFim, LocalTime horaFim) {}
