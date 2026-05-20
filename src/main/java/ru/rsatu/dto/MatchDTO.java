package ru.rsatu.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class MatchDTO {
    private Long id;
    private TeamDTO teamGuest;
    private TeamDTO teamHost;
    private RefereeDTO referee;
    private CityDTO city;
    private Integer stageType;
    private Integer phaseType;
    private Integer guestCount;
    private Integer hostCount;
    private LocalDateTime dateTime;
}