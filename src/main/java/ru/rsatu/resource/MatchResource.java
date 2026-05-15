
package ru.rsatu.resource;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.rsatu.services.MatchService;
import ru.rsatu.dto.view.MatchViewDTO;
import ru.rsatu.dto.save.MatchSaveDTO;

@Path("/matches")
@ApplicationScoped
public class MatchResource {

    private final MatchService matchService;

    @Inject
    public MatchResource(MatchService matchService) {
        this.matchService = matchService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MatchViewDTO getById(@PathParam("id") Long id) {
        return matchService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MatchViewDTO> getAll() {
        return matchService.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MatchViewDTO create(MatchSaveDTO dto) {
        return matchService.create(dto);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MatchViewDTO update(MatchSaveDTO dto) {
        return matchService.update(dto);
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(@PathParam("id") Long id) {
        matchService.deleteById(id);
        return Response.noContent().build();
    }
}
