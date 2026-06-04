package ru.rsatu.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MatchAvailabilityRequest {
    private Long teamGuestId;
    private Long teamHostId;
    private Long refereeId;
    private LocalDateTime dateTime;
    private Long excludeMatchId; // для редактирования, чтобы исключить текущий матч
}