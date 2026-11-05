package grupo04.catalogo.platform.error.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import grupo04.catalogo.platform.error.ErroHttp;
import grupo04.catalogo.platform.error.exception.ServicoFeriadosIndisponiveisException;

@Provider
public class ServicoFeriadosIndisponiveisMapper implements ExceptionMapper<ServicoFeriadosIndisponiveisException> {
    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ServicoFeriadosIndisponiveisException exception) {
        return ErroHttp.resposta(409, "SERVICO_INDISPONIVEL", exception.getMessage(),
                uriInfo.getPath());
    }
}
