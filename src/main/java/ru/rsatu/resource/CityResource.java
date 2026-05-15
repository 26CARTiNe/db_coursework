
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
import ru.rsatu.services.CityService;
import ru.rsatu.dto.view.CityViewDTO;
import ru.rsatu.dto.save.CitySaveDTO;

@Path("/cities")
@ApplicationScoped
public class CityResource {

    private final CityService cityService;

    @Inject
    public CityResource(CityService cityService) {
        this.cityService = cityService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CityViewDTO getById(@PathParam("id") Long id) {
        return cityService.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CityViewDTO> getAll() {
        return cityService.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CityViewDTO create(CitySaveDTO dto) {
        return cityService.create(dto);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CityViewDTO update(CitySaveDTO dto) {
        return cityService.update(dto);
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(@PathParam("id") Long id) {
        cityService.deleteById(id);
        return Response.noContent().build();
    }
}
