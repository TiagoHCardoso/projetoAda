package grupo04.catalogo.agendamento.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "agendamento")
public class AgendamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 120)
    public String nome;

    @Column(nullable = false, length = 120)
    public String dataInicio;

    @Column(nullable = false, length = 120)
    public String horaInicio;

    @Column(nullable = true, length = 120)
    public String dataFim;

    @Column(nullable = true, length = 120)
    public String horaFim;


    // Construtor sem argumentos: exigido pelo Jakarta Persistence.
    public AgendamentoEntity() {
    }

    public AgendamentoEntity(String nome, String dataInicio, String horaInicio, String dataFim, String horaFim) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.horaInicio = horaInicio;
        this.dataFim = dataFim;
        this.horaFim = horaFim;
    }
}
