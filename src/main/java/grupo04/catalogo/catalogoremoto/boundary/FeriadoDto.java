package grupo04.catalogo.catalogoremoto.boundary;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeriadoDto
{
    private Long id;
    private String nome;
    private LocalDate data;
}
