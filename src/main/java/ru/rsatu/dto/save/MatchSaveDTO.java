package ru.rsatu.dto.save;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchSaveDTO {
    private Long id;
    private Long teamGuestId;
    private Long teamHostId;
    private Long refereeId;
    private Long cityId;
}
