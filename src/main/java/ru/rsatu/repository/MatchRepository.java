package ru.rsatu.repository;

import java.time.LocalDateTime;
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

    public List<MatchEntity> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return entityManager.createQuery(
            "SELECT m FROM MatchEntity m WHERE m.dateTime >= :start AND m.dateTime < :end ORDER BY m.dateTime",
            MatchEntity.class)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();
    }

    public List<MatchEntity> findByStageType(int stageType) {
        return entityManager.createQuery(
            "SELECT m FROM MatchEntity m WHERE m.stageType = :stageType", 
            MatchEntity.class)
            .setParameter("stageType", stageType)
            .getResultList();
    }

    public List<MatchEntity> findByStageTypeAndPhaseType(int stageType, int phaseType) {
        return entityManager.createQuery(
            "SELECT m FROM MatchEntity m WHERE m.stageType = :stageType AND m.phaseType = :phaseType", 
            MatchEntity.class)
            .setParameter("stageType", stageType)
            .setParameter("phaseType", phaseType)
            .getResultList();
    }

    public List<MatchEntity> findByPhaseType(Integer phaseType) {
        return entityManager.createQuery(
            "SELECT m FROM MatchEntity m WHERE m.phaseType = :phaseType", 
            MatchEntity.class)
            .setParameter("phaseType", phaseType)
            .getResultList();
    }

    public List<MatchEntity> findActiveMatches() {
        return entityManager.createQuery(
            "SELECT m FROM MatchEntity m WHERE m.phaseType != 1 AND m.phaseType != 2",
            MatchEntity.class)
            .getResultList();
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

    public void deleteByStageType(int stageType) {
        entityManager.createQuery("DELETE FROM MatchEntity WHERE stageType = :stageType")
            .setParameter("stageType", stageType)
            .executeUpdate();
    }
}
