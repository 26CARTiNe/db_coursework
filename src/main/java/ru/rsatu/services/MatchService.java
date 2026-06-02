package ru.rsatu.services;

import io.quarkus.scheduler.Scheduled;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import ru.rsatu.entity.MatchEntity;
import ru.rsatu.dto.MatchDTO;
import ru.rsatu.entity.MatchPhaseType;
import ru.rsatu.entity.MatchStageType;
import ru.rsatu.entity.TeamEntity;
import ru.rsatu.repository.CityRepository;
import ru.rsatu.repository.MatchRepository;
import ru.rsatu.repository.RefereeRepository;
import ru.rsatu.repository.TeamRepository;

@ApplicationScoped
public class MatchService implements IMatchService {

    @Inject
    MatchRepository matchRepository;
    @Inject
    TeamRepository teamRepository;
    @Inject
    CityRepository cityRepository;
    @Inject
    RefereeRepository refereeRepository;
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

    public List<MatchDTO> getByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        
        List<MatchEntity> matches = matchRepository.findByDateRange(startOfDay, endOfDay);
        return matches.stream().map(this::toDTO).toList();
    }

    @Scheduled(every = "60s")
    public void updateMatchesStatus() {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        LocalDateTime currentDateTime = now.toLocalDateTime();
        
        List<MatchEntity> activeMatches = matchRepository.findActiveMatches();
        
        for (MatchEntity match : activeMatches) {
            LocalDateTime matchDateTime = match.getDateTime();
            if (matchDateTime == null) continue;
            
            Integer newPhaseType = null;
            
            if (matchDateTime.isAfter(currentDateTime)) {
                newPhaseType = 4; // Запланирован
            }
            else if (matchDateTime.isBefore(currentDateTime) && 
                     matchDateTime.plusHours(3).isAfter(currentDateTime)) {
                newPhaseType = 3; // Идет
            }
            else if (matchDateTime.plusHours(3).isBefore(currentDateTime)) {
                newPhaseType = 1; // Завершен
            }
            
            if (newPhaseType != null && !newPhaseType.equals(match.getPhaseType())) {
                match.setPhaseType(newPhaseType);
                
                if (newPhaseType == 4 || newPhaseType == 2) {
                    match.setHostCount(0);
                    match.setGuestCount(0);
                }
                
                matchRepository.save(match);
            }
        }
    }

    private void resetScoresIfNeeded(MatchEntity match) {
        Integer phaseType = match.getPhaseType();
        
        if (phaseType == 2 || phaseType == 4) {
            match.setHostCount(0);
            match.setGuestCount(0);
        }
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
        entity.setStageType(dto.getStageType());
        entity.setPhaseType(dto.getPhaseType());
        entity.setGuestCount(dto.getGuestCount());
        entity.setHostCount(dto.getHostCount());
        entity.setDateTime(dto.getDateTime());
        
        if (dto.getTeamGuest() != null && dto.getTeamGuest().getId() != null) {
            entity.setTeamGuest(teamRepository.findById(dto.getTeamGuest().getId()));
        }
        if (dto.getTeamHost() != null && dto.getTeamHost().getId() != null) {
            entity.setTeamHost(teamRepository.findById(dto.getTeamHost().getId()));
        }
        
        if (dto.getReferee() != null && dto.getReferee().getId() != null) {
            entity.setReferee(refereeRepository.findById(dto.getReferee().getId()));
        }
        
        if (dto.getCity() != null && dto.getCity().getId() != null) {
            entity.setCity(cityRepository.findById(dto.getCity().getId()));
        } else {
            throw new IllegalArgumentException("City is required for match");
        }
        
        return entity;
    }

    public List<MatchDTO> getAll() {
        return matchRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public MatchDTO create(MatchDTO dto) {
        dto.setId(null);
        MatchEntity entity = toEntity(dto);
        
        resetScoresIfNeeded(entity);
        
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

        Integer oldHostScore = entity.getHostCount();
        Integer oldGuestScore = entity.getGuestCount();
        Integer oldPhaseType = entity.getPhaseType();

        entity.setTeamGuest(teamRepository.findById(dto.getTeamGuest().getId()));
        entity.setTeamHost(teamRepository.findById(dto.getTeamHost().getId()));
        entity.setReferee(refereeRepository.findById(dto.getReferee().getId()));
        entity.setCity(cityRepository.findById(dto.getCity().getId()));
        entity.setStageType(dto.getStageType());
        entity.setPhaseType(dto.getPhaseType());
        entity.setGuestCount(dto.getGuestCount());
        entity.setHostCount(dto.getHostCount());
        entity.setDateTime(dto.getDateTime());

        resetScoresIfNeeded(entity);
        
        boolean isNowFinished = dto.getPhaseType() == 1; // ЗАКОНЧЕН
        boolean wasFinished = oldPhaseType == 1;
        
        if (isNowFinished && !wasFinished) {
            updateTeamStats(entity, dto.getHostCount(), dto.getGuestCount());
        } else if (isNowFinished && wasFinished && 
                   (!oldHostScore.equals(dto.getHostCount()) || !oldGuestScore.equals(dto.getGuestCount()))) {
            revertTeamStats(entity, oldHostScore, oldGuestScore);
            updateTeamStats(entity, dto.getHostCount(), dto.getGuestCount());
        } else if (!isNowFinished && wasFinished) {
            revertTeamStats(entity, oldHostScore, oldGuestScore);
        }

        matchRepository.save(entity);
        return toDTO(entity);
    }

    private void updateTeamStats(MatchEntity match, Integer hostScore, Integer guestScore) {
        TeamEntity hostTeam = match.getTeamHost();
        TeamEntity guestTeam = match.getTeamGuest();
        
        if (hostScore > guestScore) {
            hostTeam.setNumOfWin(hostTeam.getNumOfWin() + 1);
            teamRepository.save(hostTeam);
        } else if (guestScore > hostScore) {
            guestTeam.setNumOfWin(guestTeam.getNumOfWin() + 1);
            teamRepository.save(guestTeam);
        }
    }

    private void revertTeamStats(MatchEntity match, Integer oldHostScore, Integer oldGuestScore) {
        TeamEntity hostTeam = match.getTeamHost();
        TeamEntity guestTeam = match.getTeamGuest();
        
        if (oldHostScore > oldGuestScore) {
            hostTeam.setNumOfWin(hostTeam.getNumOfWin() - 1);
            teamRepository.save(hostTeam);
        } else if (oldGuestScore > oldHostScore) {
            guestTeam.setNumOfWin(guestTeam.getNumOfWin() - 1);
            teamRepository.save(guestTeam);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        matchRepository.deleteById(id);
    }


    @Transactional
    public MatchDTO finishMatch(Long matchId, Integer homeScore, Integer guestScore) {
        MatchEntity match = matchRepository.findById(matchId);
        if (match == null) {
            throw new NotFoundException("Match with id " + matchId + " not found");
        }
        
        match.setHostCount(homeScore);
        match.setGuestCount(guestScore);
        match.setPhaseType(MatchPhaseType.ENDED.getValue());
        
        TeamEntity hostTeam = match.getTeamHost();
        TeamEntity guestTeam = match.getTeamGuest();
        
        if (homeScore > guestScore) {
            hostTeam.setNumOfWin(hostTeam.getNumOfWin() + 1);
        } else if (guestScore > homeScore) {
            guestTeam.setNumOfWin(guestTeam.getNumOfWin() + 1);
        }
        
        teamRepository.save(hostTeam);
        teamRepository.save(guestTeam);
        matchRepository.save(match);
        
        return toDTO(match);
    }
}
