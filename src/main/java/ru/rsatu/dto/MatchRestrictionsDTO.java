package ru.rsatu.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class MatchRestrictionsDTO {
    private Long teamId;
    private Long refereeId;
    private LocalDate date;
}