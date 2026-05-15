
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
import ru.rsatu.services.TeamService;
import ru.rsatu.dto.view.TeamViewDTO;
import ru.rsatu.dto.save.TeamSaveDTO;

@Path("/teams")
@ApplicationScoped
public class TeamResource {

    private final TeamService teamService;

    @Inject
    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TeamViewDTO getById(@PathParam("id") Long id) {
        return teamService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TeamViewDTO> getAll() {
        return teamService.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TeamViewDTO create(TeamSaveDTO dto) {
        return teamService.create(dto);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TeamViewDTO update(TeamSaveDTO dto) {
        return teamService.update(dto);
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(@PathParam("id") Long id) {
        teamService.deleteById(id);
        return Response.noContent().build();
    }
}
