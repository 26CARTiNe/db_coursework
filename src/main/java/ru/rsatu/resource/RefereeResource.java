
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
import ru.rsatu.services.RefereeService;
import ru.rsatu.dto.view.RefereeViewDTO;
import ru.rsatu.dto.save.RefereeSaveDTO;

@Path("/referees")
@ApplicationScoped
public class RefereeResource {

    private final RefereeService refereeService;

    @Inject
    public RefereeResource(RefereeService refereeService) {
        this.refereeService = refereeService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RefereeViewDTO getById(@PathParam("id") Long id) {
        return refereeService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RefereeViewDTO> getAll() {
        return refereeService.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RefereeViewDTO create(RefereeSaveDTO dto) {
        return refereeService.create(dto);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RefereeViewDTO update(RefereeSaveDTO dto) {
        return refereeService.update(dto);
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(@PathParam("id") Long id) {
        refereeService.deleteById(id);
        return Response.noContent().build();
    }
}
