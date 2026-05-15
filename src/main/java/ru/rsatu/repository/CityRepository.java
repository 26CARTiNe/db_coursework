package ru.rsatu.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import ru.rsatu.db.entity.CityEntity;

@ApplicationScoped
public class CityRepository implements RepositoryInterface<CityEntity> {
    EntityManager entityManager;

    @Inject
    CityRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public CityEntity findById(Long id) {
        return entityManager.find(CityEntity.class, id);
    }

    @Override
    public List<CityEntity> findAll() {
        TypedQuery<CityEntity> query = entityManager.createQuery("SELECT e FROM CityEntity e",
                CityEntity.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void save(CityEntity entity) {
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
        CityEntity entity = findById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
