package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import ru.rsatu.db.entity.CityEntity;
import ru.rsatu.db.entity.MatchEntity;
import ru.rsatu.db.entity.RefereeEntity;
import ru.rsatu.db.entity.TeamEntity;
import ru.rsatu.mapper.MatchMapper;
import ru.rsatu.repository.MatchRepository;
import ru.rsatu.dto.view.MatchViewDTO;
import ru.rsatu.dto.save.MatchSaveDTO;

@ApplicationScoped
public class MatchService implements ServiceInterface<MatchViewDTO, MatchSaveDTO, MatchEntity> {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final EntityManager entityManager;

    @Inject
    MatchService(MatchRepository matchRepository,
            MatchMapper matchMapper,
            EntityManager entityManager) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.entityManager = entityManager;
    }

    public MatchViewDTO getById(Long id) {
        MatchEntity entity = matchRepository.findById(id);

        if (entity == null) {
            throw new NotFoundException("Match with id = " + id + " not found");
        }

        return matchMapper.toDTO(entity);
    }

    public List<MatchViewDTO> getAll() {
        return matchRepository.findAll().stream().map(matchMapper::toDTO).toList();
    }

    public MatchViewDTO create(MatchSaveDTO dto) {
        MatchEntity entity = matchMapper.toEntity(dto);

        matchRepository.save(entity);
        return matchMapper.toDTO(entity);
    }

    public MatchViewDTO update(MatchSaveDTO dto) {
        MatchEntity entity = matchRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("Match with id = " + dto.getId() + " not found");
        }

        entity.setTeamGuest(entityManager.getReference(TeamEntity.class, dto.getTeamGuestId()));
        entity.setTeamHost(entityManager.getReference(TeamEntity.class, dto.getTeamHostId()));
        entity.setReferee(entityManager.getReference(RefereeEntity.class, dto.getRefereeId()));
        entity.setCity(entityManager.getReference(CityEntity.class, dto.getCityId()));

        matchRepository.save(entity);
        return matchMapper.toDTO(entity);
    }

    public void deleteById(Long id) {
        matchRepository.deleteById(id);
    }
}
