package ru.rsatu.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import ru.rsatu.entity.MatchEntity;

@ApplicationScoped
public class MatchRepository implements RepositoryInterface<MatchEntity> {
    EntityManager entityManager;

    @Inject
    public MatchRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public MatchEntity findById(Long id) {
        return entityManager.find(MatchEntity.class, id);
    }

    @Override
    public List<MatchEntity> findAll() {
        TypedQuery<MatchEntity> query = entityManager.createQuery("SELECT e FROM MatchEntity e",
                MatchEntity.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void save(MatchEntity entity) {
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
        MatchEntity entity = findById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

}