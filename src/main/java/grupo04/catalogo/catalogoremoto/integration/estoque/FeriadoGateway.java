package grupo04.catalogo.catalogoremoto.integration.estoque;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/feriados")
@RegisterRestClient(configKey = "catalogo-local")
public interface FeriadoGateway {
    @GET
    Catalogo feriados();
}
