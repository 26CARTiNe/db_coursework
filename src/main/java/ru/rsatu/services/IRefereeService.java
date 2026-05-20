package ru.rsatu.services;

import ru.rsatu.entity.RefereeEntity;
import ru.rsatu.dto.RefereeDTO;
import java.util.List;

public interface IRefereeService {
    RefereeDTO getById(Long id);

    RefereeDTO toDTO(RefereeEntity entity);

    RefereeEntity toEntity(RefereeDTO dto);

    List<RefereeDTO> getAll();

    RefereeDTO create(RefereeDTO dto);

    RefereeDTO update(RefereeDTO dto);

    void deleteById(Long id);
}