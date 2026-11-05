package grupo04.catalogo.platform.info;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogoInfoResource {

    @ConfigProperty(name = "catalogo.nome")
    String nome;

    @GET
    public Informacoes info() {
        return new Informacoes(nome, "microservico");
    }

    public record Informacoes(String nome, String tipo) {}
}
