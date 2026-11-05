package grupo04.catalogo.agendamento.boundary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import grupo04.catalogo.agendamento.domain.AgendamentoBo;

public record AgendamentoResponseDto(Long id, String nome, LocalDate dataInicio, LocalTime horaInicio, LocalDate dataFim, LocalTime horaFim) {
    public static AgendamentoResponseDto from(AgendamentoBo produtoBo) {
        return new AgendamentoResponseDto(produtoBo.id(), produtoBo.nome(), produtoBo.dataInicio(), produtoBo.horaInicio(), produtoBo.dataFim(), produtoBo.horaFim());
    }
}
