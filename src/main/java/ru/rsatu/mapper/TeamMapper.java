package ru.rsatu.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.rsatu.db.entity.CityEntity;
import ru.rsatu.db.entity.TeamEntity;
import ru.rsatu.dto.save.TeamSaveDTO;
import ru.rsatu.dto.view.TeamViewDTO;

@Mapper(componentModel = "jakarta")
public abstract class TeamMapper {
    @Inject
    protected EntityManager entityManager;

    public abstract TeamViewDTO toDTO(TeamEntity entity);

    public abstract TeamEntity toEntity(TeamSaveDTO dto);

    @AfterMapping
    protected void mapRelations(@MappingTarget TeamEntity entity, TeamSaveDTO dto) {
        entity.setCity(entityManager.getReference(CityEntity.class, dto.getCityId()));
    }
}
