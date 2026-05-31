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
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.rsatu.services.IRefereeService;
import ru.rsatu.dto.RefereeDTO;

@Path("api/referees")
@ApplicationScoped
public class RefereeResource {

    @Inject
    IRefereeService refereeService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RefereeDTO getById(@PathParam("id") Long id) {
        return refereeService.getById(id);
    }

    @GET
    public List<RefereeDTO> getAll() {
        return refereeService.getAll();
    }

    @POST
    @RolesAllowed("ADMIN")
    public RefereeDTO create(RefereeDTO dto) {
        return refereeService.create(dto);
    }

    @PUT
    @RolesAllowed("ADMIN")
    public RefereeDTO update(RefereeDTO dto) {
        return refereeService.update(dto);
    }

    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @DELETE
    public Response deleteById(@PathParam("id") Long id) {
        refereeService.deleteById(id);
        return Response.noContent().build();
    }
}