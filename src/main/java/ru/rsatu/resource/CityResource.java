package ru.rsatu.resource;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import ru.rsatu.services.CityService;
import ru.rsatu.dto.CityDTO;

@Path("api/cities")
@ApplicationScoped
public class CityResource {

    @Inject
    CityService cityService;

    @GET
    @Path("/{id}")
    public CityDTO getById(@PathParam("id") Long id) {
        return cityService.getById(id);
    }

    @GET
    public List<CityDTO> getAll() {
        return cityService.getAll();
    }

    @POST
    public CityDTO create(CityDTO dto) {
        return cityService.create(dto);
    }

    @PUT
    public CityDTO update(CityDTO dto) {
        return cityService.update(dto);
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(@PathParam("id") Long id) {
        cityService.deleteById(id);
        return Response.noContent().build();
    }
}