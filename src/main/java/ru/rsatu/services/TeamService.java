package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import ru.rsatu.db.entity.CityEntity;
import ru.rsatu.db.entity.TeamEntity;
import ru.rsatu.mapper.TeamMapper;
import ru.rsatu.repository.TeamRepository;
import ru.rsatu.dto.save.TeamSaveDTO;
import ru.rsatu.dto.view.TeamViewDTO;

@ApplicationScoped
public class TeamService implements ServiceInterface<TeamViewDTO, TeamSaveDTO, TeamEntity> {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final EntityManager entityManager;

    @Inject
    TeamService(TeamRepository teamRepository,
            TeamMapper teamMapper,
            EntityManager entityManager) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.entityManager = entityManager;
    }

    public TeamViewDTO getById(Long id) {
        TeamEntity entity = teamRepository.findById(id);

        if (entity == null) {
            throw new NotFoundException("Team with id = " + id + " not found");
        }

        return teamMapper.toDTO(entity);
    }

    public List<TeamViewDTO> getAll() {
        return teamRepository.findAll().stream().map(teamMapper::toDTO).toList();
    }

    @Transactional
    public TeamViewDTO create(TeamSaveDTO dto) {
        dto.setId(null);
        TeamEntity entity = teamMapper.toEntity(dto);

        teamRepository.save(entity);
        return teamMapper.toDTO(entity);
    }

    @Transactional
    public TeamViewDTO update(TeamSaveDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }

        TeamEntity entity = teamRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("Team with id = " + dto.getId() + " not found");
        }

        entity.setCity(entityManager.getReference(CityEntity.class, dto.getCityId()));

        teamRepository.save(entity);
        return teamMapper.toDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

}
