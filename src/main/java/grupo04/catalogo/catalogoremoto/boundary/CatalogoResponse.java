package grupo04.catalogo.catalogoremoto.boundary;

import grupo04.catalogo.catalogoremoto.domain.CatalogoResultado;

import java.util.List;

public record CatalogoResponse(List<FeriadoDto> feriados, int total, String origem) {

    public static CatalogoResponse from(CatalogoResultado resultado) {
        List<FeriadoDto> feriados = resultado.feriados().stream()
                .map(p -> new FeriadoDto(p.id(), p.nome(), p.data()))
                .toList();
        return new CatalogoResponse(feriados, feriados.size(), resultado.origem());
    }
}
