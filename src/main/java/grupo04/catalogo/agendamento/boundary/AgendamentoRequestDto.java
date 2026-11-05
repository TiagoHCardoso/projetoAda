package grupo04.catalogo.agendamento.boundary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequestDto(
        @NotBlank(message = "Nome é obrigatório") @Size(max = 120) String nome,
        LocalDate dataInicio, 
        LocalTime horaInicio, 
        LocalDate dataFim, 
        LocalTime horaFim
) {
}
