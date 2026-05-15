package ru.rsatu.mapper;

import org.mapstruct.Mapper;

import ru.rsatu.dto.save.CitySaveDTO;
import ru.rsatu.dto.view.CityViewDTO;
import ru.rsatu.db.entity.CityEntity;

@Mapper(componentModel = "jakarta")
public abstract class CityMapper {
    public abstract CityViewDTO toDTO(CityEntity entity);

    public abstract CityEntity toEntity(CitySaveDTO dto);
}
