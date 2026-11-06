package grupo04.catalogo.platform.error.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import grupo04.catalogo.agendamento.domain.DiaIndisponivelException;
import grupo04.catalogo.platform.error.ErroHttp;

@Provider
public class DiaIndisponivelMapper implements ExceptionMapper<DiaIndisponivelException> {
    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(DiaIndisponivelException exception) {
        return ErroHttp.resposta(409, "DIA_INDISPONIVEL", exception.getMessage(),
                uriInfo.getPath());
    }
}
