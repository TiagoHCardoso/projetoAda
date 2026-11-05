package grupo04.catalogo.platform.error.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import grupo04.catalogo.platform.error.ErroHttp;
import grupo04.catalogo.platform.error.exception.HorariosSobrepostosException;

@Provider
public class HorariosSobrepostosMapper implements ExceptionMapper<HorariosSobrepostosException> {
    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(HorariosSobrepostosException exception) {
        return ErroHttp.resposta(409, "HORARIOS_SOBREPOSTOS", exception.getMessage(),
                uriInfo.getPath());
    }
}
