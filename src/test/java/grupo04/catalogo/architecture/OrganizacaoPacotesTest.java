package grupo04.catalogo.architecture;

import jakarta.persistence.Entity;
import org.junit.jupiter.api.Test;

import grupo04.catalogo.agendamento.boundary.AgendamentoResource;
import grupo04.catalogo.agendamento.domain.AgendamentoBo;
import grupo04.catalogo.agendamento.domain.AgendamentoService;
import grupo04.catalogo.agendamento.repository.AgendamentoRepository;
import grupo04.catalogo.catalogoremoto.boundary.FeriadoResource;
import grupo04.catalogo.catalogoremoto.domain.CatalogoResultado;
import grupo04.catalogo.catalogoremoto.domain.FeriadoService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Teste simples de arquitetura, sem nova biblioteca: garante limites básicos do exemplo BDR. */
class OrganizacaoPacotesTest {

    @Test
    void pacotesSeguemFeatureMaisBoundaryDomainRepository() {
        assertEquals("grupo04.catalogo.produtos.boundary", AgendamentoResource.class.getPackageName());
        assertEquals("grupo04.catalogo.produtos.domain", AgendamentoService.class.getPackageName());
        assertEquals("grupo04.catalogo.produtos.repository", AgendamentoRepository.class.getPackageName());
        assertEquals("grupo04.catalogo.catalogoremoto.boundary", FeriadoResource.class.getPackageName());
        assertEquals("grupo04.catalogo.catalogoremoto.domain", FeriadoService.class.getPackageName());
    }

    @Test
    void servicosDeDominioNaoRetornamDtoHttp() throws NoSuchMethodException {
        assertEquals(AgendamentoBo.class,
                AgendamentoService.class.getMethod("criar", String.class, BigDecimal.class, int.class).getReturnType());
        assertEquals(CatalogoResultado.class,
                FeriadoService.class.getMethod("getProducts").getReturnType());
        assertNull(AgendamentoBo.class.getAnnotation(Entity.class), "O modelo de domínio não deve ser entidade JPA");
    }
}
