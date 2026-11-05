package grupo04.catalogo.platform.error.exception;

public class ServicoFeriadosIndisponiveisException extends RuntimeException {
    public ServicoFeriadosIndisponiveisException() {
        super("Servico de consulta de feriados indisponivel.");
    }
}
