package ru.rsatu.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import ru.rsatu.entity.TeamEntity;

@ApplicationScoped
public class TeamRepository implements RepositoryInterface<TeamEntity> {
    EntityManager entityManager;

    @Inject
    public TeamRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public TeamEntity findById(Long id) {
        return entityManager.find(TeamEntity.class, id);
    }

    @Override
    public List<TeamEntity> findAll() {
        TypedQuery<TeamEntity> query = entityManager.createQuery("SELECT e FROM TeamEntity e",
                TeamEntity.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void save(TeamEntity entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
        entityManager.flush();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        TeamEntity entity = findById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

}