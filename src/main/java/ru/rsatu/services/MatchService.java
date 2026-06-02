package ru.rsatu.services;

import io.quarkus.scheduler.Scheduled;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import ru.rsatu.entity.CityEntity;
import ru.rsatu.entity.MatchEntity;
import ru.rsatu.dto.MatchDTO;
import ru.rsatu.entity.MatchPhaseType;
import ru.rsatu.entity.MatchStageType;
import ru.rsatu.entity.RefereeEntity;
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

    @Scheduled(every = "5s")
    public void updateMatchesStatus() {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        LocalDateTime currentDateTime = now.toLocalDateTime();
        
        List<MatchEntity> matchesToUpdate = matchRepository.findScheduledOrLiveMatches();
        
        for (MatchEntity match : matchesToUpdate) {
            LocalDateTime matchDateTime = match.getDateTime();
            if (matchDateTime == null) continue;
            
            Integer oldPhaseType = match.getPhaseType();
            Integer newPhaseType = null;
            
            if (matchDateTime.isAfter(currentDateTime)) {
                newPhaseType = 4; // Запланирован
                if (match.getHostCount() != 0 || match.getGuestCount() != 0) {
                    match.setHostCount(0);
                    match.setGuestCount(0);
                }
            }
            else if (matchDateTime.isBefore(currentDateTime) && 
                     matchDateTime.plusHours(3).isAfter(currentDateTime)) {
                newPhaseType = 3; // Идет
            }
            else if (matchDateTime.plusHours(3).isBefore(currentDateTime)) {
                newPhaseType = 1; // Завершен
            }
            
            if (newPhaseType != null && !newPhaseType.equals(oldPhaseType)) {
                match.setPhaseType(newPhaseType);
                matchRepository.save(match);
            }
        }
        
        for (int stage = 2; stage <= 4; stage++) {
            tryGenerateNextRound(stage);
        }
    }

    private void tryGenerateNextRound(int currentStage) {
        List<MatchEntity> allMatchesInStage = matchRepository.findByStageType(currentStage);
        
        if (allMatchesInStage.isEmpty()) {
            return;
        }
        
        boolean allFinished = allMatchesInStage.stream()
            .allMatch(match -> match.getPhaseType() == 1);
        
        if (!allFinished) {
            return;
        }
        
        int nextStage = currentStage + 1;
        List<MatchEntity> nextStageMatches = matchRepository.findByStageType(nextStage);
        
        if (nextStageMatches.isEmpty()) {
            generateNextRound(currentStage);
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
        
        boolean isNowFinished = dto.getPhaseType() == 1;
        boolean wasFinished = oldPhaseType == 1;
        
        if (isNowFinished && !wasFinished) {
            updateTeamStats(entity, dto.getHostCount(), dto.getGuestCount());
            
            int currentStage = dto.getStageType();
            if (currentStage == 2 || currentStage == 3 || currentStage == 4) {
                generateNextRound(currentStage);
            }
        } else if (isNowFinished && wasFinished && 
                   (!oldHostScore.equals(dto.getHostCount()) || !oldGuestScore.equals(dto.getGuestCount()))) {
            revertTeamStats(entity, oldHostScore, oldGuestScore);
            updateTeamStats(entity, dto.getHostCount(), dto.getGuestCount());
        } else if (!isNowFinished && wasFinished) {
            revertTeamStats(entity, oldHostScore, oldGuestScore);
        }

        if (dto.getPhaseType() == 2 && entity.getStageType() >= 2) {
            throw new IllegalArgumentException("❌ Нельзя отменить матч плей-офф! Только завершить или перенести");
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
        
        int currentStage = match.getStageType();
        if (currentStage == 2 || currentStage == 3 || currentStage == 4) {
            generateNextRound(currentStage);
        }
        
        return toDTO(match);
    }

    @Transactional
    public void generateNextRound(int currentStage) {
        int nextStage = currentStage + 1;
        
        List<MatchEntity> finishedMatches = matchRepository.findByStageTypeAndPhaseType(currentStage, 1);
        
        if (finishedMatches.isEmpty()) {
            return;
        }
        
        List<MatchEntity> allMatchesInStage = matchRepository.findByStageType(currentStage);
        if (finishedMatches.size() != allMatchesInStage.size()) {
            return;
        }
        
        List<TeamEntity> winners = new ArrayList<>();
        for (MatchEntity match : finishedMatches) {
            TeamEntity winner = null;
            if (match.getHostCount() > match.getGuestCount()) {
                winner = match.getTeamHost();
            } else if (match.getGuestCount() > match.getHostCount()) {
                winner = match.getTeamGuest();
            }
            if (winner != null) {
                winners.add(winner);
            }
        }
        
        if (winners.size() % 2 != 0) {
            return;
        }
        
        matchRepository.deleteByStageType(nextStage);
        
        Random random = new Random();
        for (int i = 0; i < winners.size(); i += 2) {
            TeamEntity team1 = winners.get(i);
            TeamEntity team2 = winners.get(i + 1);
            
            CityEntity city = team1.getCity();
            
            List<RefereeEntity> availableReferees = refereeRepository.findRefereesNotFromCities(
                team1.getCity().getId(), 
                team2.getCity().getId()
            );
            
            RefereeEntity referee = null;
            if (!availableReferees.isEmpty()) {
                referee = availableReferees.get(random.nextInt(availableReferees.size()));
            } else {
                referee = refereeRepository.findAll().stream().findFirst().orElse(null);
            }
            
            if (referee == null) {
                continue;
            }
            
            MatchEntity newMatch = new MatchEntity();
            newMatch.setTeamHost(team1);
            newMatch.setTeamGuest(team2);
            newMatch.setReferee(referee);
            newMatch.setCity(city);
            newMatch.setStageType(nextStage);
            newMatch.setPhaseType(4);
            newMatch.setHostCount(0);
            newMatch.setGuestCount(0);
            newMatch.setDateTime(LocalDateTime.now().plusDays(7));
            
            matchRepository.save(newMatch);
        }
    }
}
