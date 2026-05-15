package ru.rsatu.repository;

import java.util.List;

public interface RepositoryInterface<Entity> {
    Entity findById(Long id);

    List<Entity> findAll();

    void save(Entity entity);

    void deleteById(Long id);
}
