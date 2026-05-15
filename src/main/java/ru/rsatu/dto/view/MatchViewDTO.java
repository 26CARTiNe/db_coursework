package ru.rsatu.dto.view;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchViewDTO {
    private Long id;
    private TeamViewDTO teamGuestId;
    private TeamViewDTO teamHostId;
    private RefereeViewDTO refereeId;
    private CityViewDTO cityId;
}
