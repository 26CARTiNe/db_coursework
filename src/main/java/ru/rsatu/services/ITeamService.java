package ru.rsatu.services;

import ru.rsatu.entity.TeamEntity;
import ru.rsatu.dto.TeamDTO;
import java.util.List;

public interface ITeamService {
    TeamDTO getById(Long id);

    TeamDTO toDTO(TeamEntity entity);

    TeamEntity toEntity(TeamDTO dto);

    List<TeamDTO> getAll();

    TeamDTO create(TeamDTO dto);

    TeamDTO update(TeamDTO dto);

    void deleteById(Long id);
}