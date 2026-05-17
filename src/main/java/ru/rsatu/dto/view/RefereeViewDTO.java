package ru.rsatu.dto.view;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefereeViewDTO {
    private Long id;
    private CityViewDTO city;
}
