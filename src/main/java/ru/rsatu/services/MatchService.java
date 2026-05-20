package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import ru.rsatu.entity.MatchEntity;
import ru.rsatu.dto.MatchDTO;
import ru.rsatu.entity.MatchPhaseType;
import ru.rsatu.entity.MatchStageType;
import ru.rsatu.repository.MatchRepository;

@ApplicationScoped
public class MatchService implements IMatchService {

    @Inject
    MatchRepository matchRepository;
    @Inject
    ICityService cityService;
    @Inject
    IRefereeService refereeService;
    @Inject
    ITeamService teamService;

    public String getMatchPhase(MatchDTO dto){
        return MatchPhaseType.getPhase(dto.getPhaseType());
    }

    public String getMatchStage(MatchDTO dto){
        return MatchStageType.getStage(dto.getStageType());
    }

    public MatchDTO getById(Long id) {
        MatchEntity entity = matchRepository.findById(id);

        if (entity == null) {
            throw new NotFoundException("Match with id = " + id + " not found");
        }

        return toDTO(entity);
    }

    @Override
    public MatchDTO toDTO(MatchEntity entity) {
        if (entity == null) {
            return null;
        }
        MatchDTO dto = new MatchDTO();
        dto.setId(entity.getId());
        dto.setTeamGuest(teamService.toDTO(entity.getTeamGuest()));
        dto.setTeamHost(teamService.toDTO(entity.getTeamHost()));
        dto.setReferee(refereeService.toDTO(entity.getReferee()));
        dto.setCity(cityService.toDTO(entity.getCity()));
        dto.setStageType(entity.getStageType());
        dto.setPhaseType(entity.getPhaseType());
        dto.setGuestCount(entity.getGuestCount());
        dto.setHostCount(entity.getHostCount());
        dto.setDateTime(entity.getDateTime());
        return dto;
    }

    @Override
    public MatchEntity toEntity(MatchDTO dto) {
        if (dto == null) {
            return null;
        }
        MatchEntity entity = new MatchEntity();
        entity.setId(dto.getId());
        entity.setTeamGuest(teamService.toEntity(dto.getTeamGuest()));
        entity.setTeamHost(teamService.toEntity(dto.getTeamHost()));
        entity.setReferee(refereeService.toEntity(dto.getReferee()));
        entity.setCity(cityService.toEntity(dto.getCity()));
        entity.setStageType(dto.getStageType());
        entity.setPhaseType(dto.getPhaseType());
        entity.setHostCount(dto.getHostCount());
        entity.setGuestCount(dto.getGuestCount());
        entity.setDateTime(dto.getDateTime());
        return entity;
    }

    public List<MatchDTO> getAll() {
        return matchRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public MatchDTO create(MatchDTO dto) {
        dto.setId(null);
        MatchEntity entity = toEntity(dto);

        matchRepository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public MatchDTO update(MatchDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }

        MatchEntity entity = matchRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("Match with id = " + dto.getId() + " not found");
        }

        matchRepository.save(toEntity(dto));
        return toDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        matchRepository.deleteById(id);
    }
}