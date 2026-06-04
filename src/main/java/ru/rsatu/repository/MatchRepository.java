package ru.rsatu.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<MatchEntity> findScheduledOrLiveMatches() {
        return entityManager.createQuery(
            "SELECT m FROM MatchEntity m WHERE m.phaseType = 4 OR m.phaseType = 3",
            MatchEntity.class)
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

    public List<MatchEntity> findByRestrictions(Long teamId, Long refereeId, LocalDate date) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM MatchEntity m WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (teamId != null) {
            jpql.append(" AND (m.teamHost.id = :teamId OR m.teamGuest.id = :teamId)");
            params.put("teamId", teamId);
        }

        if (refereeId != null) {
            jpql.append(" AND m.referee.id = :refereeId");
            params.put("refereeId", refereeId);
        }

        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
            jpql.append(" AND m.dateTime >= :start AND m.dateTime < :end");
            params.put("start", startOfDay);
            params.put("end", endOfDay);
        }

        jpql.append(" ORDER BY m.dateTime");

        TypedQuery<MatchEntity> query = entityManager.createQuery(jpql.toString(), MatchEntity.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }

    public boolean isTeamBusyOnDate(Long teamId, LocalDate date, Long excludeMatchId) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        String jpql = "SELECT COUNT(m) FROM MatchEntity m " +
                "WHERE (m.teamHost.id = :teamId OR m.teamGuest.id = :teamId) " +
                "AND m.dateTime >= :start AND m.dateTime < :end " +
                "AND m.phaseType != 2"; // не отменён

        if (excludeMatchId != null) {
            jpql += " AND m.id != :excludeMatchId";
        }

        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("teamId", teamId);
        query.setParameter("start", startOfDay);
        query.setParameter("end", endOfDay);
        if (excludeMatchId != null) {
            query.setParameter("excludeMatchId", excludeMatchId);
        }

        return query.getSingleResult() > 0;
    }

    public boolean isRefereeBusyOnDate(Long refereeId, LocalDate date, Long excludeMatchId) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        String jpql = "SELECT COUNT(m) FROM MatchEntity m " +
                "WHERE m.referee.id = :refereeId " +
                "AND m.dateTime >= :start AND m.dateTime < :end " +
                "AND m.phaseType != 2";

        if (excludeMatchId != null) {
            jpql += " AND m.id != :excludeMatchId";
        }

        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("refereeId", refereeId);
        query.setParameter("start", startOfDay);
        query.setParameter("end", endOfDay);
        if (excludeMatchId != null) {
            query.setParameter("excludeMatchId", excludeMatchId);
        }

        return query.getSingleResult() > 0;
    }
}
