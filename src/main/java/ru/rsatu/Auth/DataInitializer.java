package ru.rsatu.Auth;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class DataInitializer {

    @Transactional
    public void init(@Observes StartupEvent ev) {
        // Проверяем, нет ли уже админа
        if (UserEntity.findByLogin("admin") == null) {
            UserEntity admin = new UserEntity();
            admin.setLogin("admin");
            // Хэш для пароля "admin123"
            admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
            admin.setRole(Role.ADMIN);

            UserEntity.persist(admin);
            System.out.println("✅ Admin user created: login=admin, password=admin123");
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }
    }
}