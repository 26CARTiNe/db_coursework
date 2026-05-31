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
import ru.rsatu.services.MatchService;
import ru.rsatu.dto.MatchDTO;

@Path("api/matches")
@ApplicationScoped
public class MatchResource {

    @Inject
    MatchService matchService;

    @GET
    @Path("/{id}")
    public MatchDTO getById(@PathParam("id") Long id) {
        return matchService.getById(id);
    }

    @GET
    public List<MatchDTO> getAll() {
        return matchService.getAll();
    }

    @POST
    @RolesAllowed("ADMIN")
    public MatchDTO create(MatchDTO dto) {
        return matchService.create(dto);
    }

    @PUT
    @RolesAllowed("ADMIN")
    public MatchDTO update(MatchDTO dto) {
        return matchService.update(dto);
    }

    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @DELETE
    public Response deleteById(@PathParam("id") Long id) {
        matchService.deleteById(id);
        return Response.noContent().build();
    }
}