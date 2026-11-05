package grupo04.catalogo.agendamento.domain;

/** A retirada não pode deixar o saldo do estoque negativo. */
public class DiaIndisponivelException extends RuntimeException {
    public DiaIndisponivelException(String data) {
        super("Dia indisponivel: " + data + ".");
    }
}
