package grupo04.catalogo.catalogoremoto.boundary;

import grupo04.catalogo.catalogoremoto.domain.FeriadoService;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/feriados")
@Produces(MediaType.APPLICATION_JSON)
public class FeriadoResource {

    @Inject
    FeriadoService feriadoService;

    @Inject
    MeterRegistry meterRegistry;

    @GET
    public Response getFeriados() {
        CatalogoResponse catalogo = CatalogoResponse.from(feriadoService.getFeriados());

        meterRegistry.counter("catalogo.consultas", "origem", catalogo.origem()).increment();

        if ("FALLBACK".equals(catalogo.origem())) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(catalogo).build();
        }
        return Response.ok(catalogo).build();
    }
}
