package grupo04.catalogo.platform.error;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

public final class ErroHttp {
    private ErroHttp() {
    }

    public static Response resposta(int status, String codigo, String mensagem, String path) {
        return resposta(status, codigo, mensagem, path, List.of());
    }

    public static Response resposta(int status, String codigo, String mensagem,
                                    String path, List<DetalheErro> detalhes) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(ErroResponse.of(codigo, mensagem, path, detalhes))
                .build();
    }
}
