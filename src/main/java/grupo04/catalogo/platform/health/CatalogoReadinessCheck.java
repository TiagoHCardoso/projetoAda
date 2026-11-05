package grupo04.catalogo.platform.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class CatalogoReadinessCheck implements HealthCheck {

    @ConfigProperty(name = "catalogo.nome")
    String nome;

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("catalogo-configurado")
                .status(nome != null && !nome.isBlank())
                .build();
    }
}
