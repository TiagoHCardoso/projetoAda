package grupo04.catalogo.catalogoremoto.domain;

import grupo04.catalogo.catalogoremoto.integration.estoque.FeriadoGateway;
import grupo04.catalogo.catalogoremoto.integration.estoque.Catalogo;
import grupo04.catalogo.catalogoremoto.domain.CatalogoResultado.FeriadoDisponivel;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class FeriadoService {

    private final FeriadoGateway feriadoGateway;

    public FeriadoService(@RestClient FeriadoGateway feriadoGateway) {
        this.feriadoGateway = feriadoGateway;
    }

    @Retry(maxRetries = 2, delay = 200)
    @Fallback(fallbackMethod = "fallbackFeriados")
    public CatalogoResultado getFeriados() {
        Catalogo catalogoExterno = feriadoGateway.feriados();

        // Consulta sem estado: retry não acumula resultados repetidos.
        List<FeriadoDisponivel> disponiveis = catalogoExterno.feriados().stream()
                .map(product -> new FeriadoDisponivel((long) product.id(), product.nome(), product.data()))
                .toList();

        return new CatalogoResultado(disponiveis, "ESTOQUE");
    }

    public CatalogoResultado fallbackFeriados() {
        return new CatalogoResultado(List.of(), "FALLBACK");
    }
}
