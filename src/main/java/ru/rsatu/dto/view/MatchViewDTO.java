package ru.rsatu.dto.view;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchViewDTO {
    private Long id;
    private TeamViewDTO teamGuest;
    private TeamViewDTO teamHost;
    private RefereeViewDTO referee;
    private CityViewDTO city;
}
