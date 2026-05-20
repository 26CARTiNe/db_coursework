package ru.rsatu.services;

import ru.rsatu.dto.MatchDTO;
import ru.rsatu.entity.MatchEntity;
import java.util.List;

public interface IMatchService {

    String getMatchPhase(MatchDTO dto);

    String getMatchStage(MatchDTO dto);

    MatchDTO getById(Long id);

    MatchDTO toDTO(MatchEntity entity);

    MatchEntity toEntity(MatchDTO dto);

    List<MatchDTO> getAll();

    MatchDTO create(MatchDTO dto);

    MatchDTO update(MatchDTO dto);

    void deleteById(Long id);

}