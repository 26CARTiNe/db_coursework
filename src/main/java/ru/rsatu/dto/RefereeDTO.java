package ru.rsatu.dto;

import lombok.*;

@Getter
@Setter
public class RefereeDTO {
    private Long id;
    private CityDTO city;
    private String FIO;
    private String license;
    private Integer stageYears;
}