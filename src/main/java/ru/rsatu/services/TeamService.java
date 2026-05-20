package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import ru.rsatu.entity.TeamEntity;
import ru.rsatu.dto.TeamDTO;
import ru.rsatu.repository.TeamRepository;

@ApplicationScoped
public class TeamService implements ITeamService {

    @Inject
    TeamRepository teamRepository;
    @Inject
    ICityService cityService;

    public TeamDTO getById(Long id) {
        TeamEntity entity = teamRepository.findById(id);

        if (entity == null) {
            throw new NotFoundException("Team with id = " + id + " not found");
        }

        return toDTO(entity);
    }

    @Override
    public TeamDTO toDTO(TeamEntity entity) {
        if (entity == null) {
            return null;
        }

        TeamDTO dto = new TeamDTO();
        dto.setId(entity.getId());
        dto.setCity(cityService.toDTO(entity.getCity()));
        dto.setName(entity.getName());
        dto.setPeoplesInTeam(entity.getPeoplesInTeam());
        dto.setNumOfWin(entity.getNumOfWin());
        return dto;
    }

    @Override
    public TeamEntity toEntity(TeamDTO dto) {
        if (dto == null) {
            return null;
        }

        TeamEntity entity = new TeamEntity();
        entity.setId(dto.getId());
        dto.setCity(cityService.toDTO(entity.getCity()));
        entity.setName(dto.getName());
        entity.setPeoplesInTeam(dto.getPeoplesInTeam());
        entity.setNumOfWin(dto.getNumOfWin());
        return entity;
    }

    public List<TeamDTO> getAll() {
        return teamRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public TeamDTO create(TeamDTO dto) {
        dto.setId(null);
        TeamEntity entity = toEntity(dto);

        teamRepository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public TeamDTO update(TeamDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }

        TeamEntity entity = teamRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("Team with id = " + dto.getId() + " not found");
        }

        teamRepository.save(toEntity(dto));
        return toDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }
}