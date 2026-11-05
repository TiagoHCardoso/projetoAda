package grupo04.catalogo.platform.error.exception;

public class HorariosSobrepostosException extends RuntimeException {
    public HorariosSobrepostosException() {
        super("Os horarios foram sobrepostos.");
    }
}
