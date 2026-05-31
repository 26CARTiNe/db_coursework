package ru.rsatu.Auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class AuthService {

    @Inject
    JwtService jwtService;

    public AuthResponse login(AuthRequest request) {
        UserEntity user = UserEntity.findByLogin(request.getLogin());

        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new NotAuthorizedException("Invalid login or password");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getRole().toString(), user.getLogin());
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        UserEntity existingUser = UserEntity.findByLogin(request.getLogin());
        if (existingUser != null) {
            throw new BadRequestException("User with this login already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setLogin(request.getLogin());
        newUser.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        newUser.setRole(Role.USER);
        newUser.persist();

        String token = jwtService.generateToken(newUser);
        return new AuthResponse(token, newUser.getRole().toString(), newUser.getLogin());
    }
}