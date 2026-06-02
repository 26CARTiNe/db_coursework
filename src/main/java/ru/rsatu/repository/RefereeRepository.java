package ru.rsatu.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import ru.rsatu.entity.RefereeEntity;

@ApplicationScoped
public class RefereeRepository implements RepositoryInterface<RefereeEntity> {
    EntityManager entityManager;

    @Inject
    RefereeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public RefereeEntity findById(Long id) {
        return entityManager.find(RefereeEntity.class, id);
    }

    @Override
    public List<RefereeEntity> findAll() {
        TypedQuery<RefereeEntity> query = entityManager.createQuery("SELECT e FROM RefereeEntity e",
                RefereeEntity.class);
        return query.getResultList();
    }

    public List<RefereeEntity> findRefereesNotFromCities(Long cityId1, Long cityId2) {
        return entityManager.createQuery(
            "SELECT r FROM RefereeEntity r WHERE r.city.id != :cityId1 AND r.city.id != :cityId2", 
            RefereeEntity.class)
            .setParameter("cityId1", cityId1)
            .setParameter("cityId2", cityId2)
            .getResultList();
    }

    @Override
    @Transactional
    public void save(RefereeEntity entity) {
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
        RefereeEntity entity = findById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
