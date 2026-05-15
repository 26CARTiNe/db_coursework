package ru.rsatu.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.rsatu.dto.save.RefereeSaveDTO;
import ru.rsatu.dto.view.RefereeViewDTO;
import ru.rsatu.db.entity.CityEntity;
import ru.rsatu.db.entity.RefereeEntity;

@Mapper(componentModel = "jakarta")
public abstract class RefereeMapper {
    @Inject
    protected EntityManager entityManager;

    public abstract RefereeViewDTO toDTO(RefereeEntity entity);

    public abstract RefereeEntity toEntity(RefereeSaveDTO dto);

    @AfterMapping
    protected void mapRelations(RefereeEntity entity, @MappingTarget RefereeSaveDTO dto) {
        entity.setCity(entityManager.getReference(CityEntity.class, dto.getCityId()));
    }
}
