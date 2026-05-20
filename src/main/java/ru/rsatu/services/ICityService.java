package ru.rsatu.services;

import ru.rsatu.entity.CityEntity;
import ru.rsatu.dto.CityDTO;
import java.util.List;

public interface ICityService {
    CityDTO getById(Long id);

    CityDTO toDTO(CityEntity entity);

    CityEntity toEntity(CityDTO dto);

    List<CityDTO> getAll();

    CityDTO create(CityDTO dto);

    CityDTO update(CityDTO dto);

    void deleteById(Long id);

}