package grupo04.catalogo.catalogoremoto.domain;

import java.time.LocalDate;
import java.util.List;

public record CatalogoResultado(List<FeriadoDisponivel> feriados, String origem) {

    public record FeriadoDisponivel(Long id, String nome, LocalDate data) {}
}
