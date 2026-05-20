package ru.rsatu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDTO {
    private Long id;
    private CityDTO city;
    private String name;
    private Integer peoplesInTeam;
    private Integer numOfWin;
}