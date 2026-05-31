package ru.rsatu.resource;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import ru.rsatu.services.TeamService;
import ru.rsatu.dto.TeamDTO;

@Path("api/teams")
@ApplicationScoped
public class TeamResource {

    @Inject
    TeamService teamService;

    @GET
    @Path("/{id}")
    public TeamDTO getById(@PathParam("id") Long id) {
        return teamService.getById(id);
    }

    @GET
    public List<TeamDTO> getAll() {
        return teamService.getAll();
    }

    @POST
    @RolesAllowed("ADMIN")
    public TeamDTO create(TeamDTO dto) {
        return teamService.create(dto);
    }

    @PUT
    @RolesAllowed("ADMIN")
    public TeamDTO update(TeamDTO dto) {
        return teamService.update(dto);
    }

    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @DELETE
    public Response deleteById(@PathParam("id") Long id) {
        teamService.deleteById(id);
        return Response.noContent().build();
    }
}