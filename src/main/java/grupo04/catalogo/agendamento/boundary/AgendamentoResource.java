package grupo04.catalogo.agendamento.boundary;

import lombok.RequiredArgsConstructor;
import grupo04.catalogo.agendamento.domain.AgendamentoBo;
import grupo04.catalogo.agendamento.domain.AgendamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;

@Path("/agendamento")
@RequiredArgsConstructor
public class AgendamentoResource {

    private final AgendamentoService service;

    @GET
    public List<AgendamentoResponseDto> listar(@QueryParam("nome") String nome) {
        return service.listar(nome).stream().map(AgendamentoResponseDto::from).toList();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        var produto = service.buscar(id);
        return produto == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(AgendamentoResponseDto.from(produto)).build();
    }

    @POST
    public Response criar(@NotNull @Valid AgendamentoRequestDto request, @Context UriInfo uriInfo) {
        AgendamentoBo criar = service.criar(request);
        AgendamentoResponseDto criado = AgendamentoResponseDto.from(criar);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(criado.id().toString()).build())
                .entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @NotNull @Valid AgendamentoRequestDto request) {
        var atualizado = service.atualizar(id, request);
        return atualizado == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(AgendamentoResponseDto.from(atualizado)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        return service.excluir(id) ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
