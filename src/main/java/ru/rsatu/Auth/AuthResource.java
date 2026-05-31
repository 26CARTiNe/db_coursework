package ru.rsatu.Auth;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;

@Path("/api/auth")
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    @PermitAll
    public AuthResponse register(@Valid RegisterRequest request) {
        return authService.register(request);
    }

    @POST
    @Path("/login")
    @PermitAll
    public AuthResponse login(@Valid AuthRequest request) {
        return authService.login(request);
    }
}