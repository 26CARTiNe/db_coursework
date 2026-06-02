package ru.rsatu.Auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.jwt.Claims;

@ApplicationScoped
public class JwtService {

    public String generateToken(UserEntity user) {
        return Jwt.issuer("basketball-championship")
                .upn(user.getLogin())
                .preferredUserName(user.getLogin())
                .subject(user.getLogin())
                .groups(user.getRole().toString())
                .claim(Claims.email.name(), user.getLogin())
                .expiresIn(300)
                .sign();
    }
}