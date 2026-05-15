package ru.rsatu.dto.save;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamSaveDTO {
    private Long id;
    private Long cityId;
}
