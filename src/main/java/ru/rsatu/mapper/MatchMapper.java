package ru.rsatu.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import ru.rsatu.dto.save.MatchSaveDTO;
import ru.rsatu.dto.view.MatchViewDTO;
import ru.rsatu.db.entity.CityEntity;
import ru.rsatu.db.entity.MatchEntity;
import ru.rsatu.db.entity.RefereeEntity;
import ru.rsatu.db.entity.TeamEntity;

@Mapper(componentModel = "jakarta")
public abstract class MatchMapper {
    @Inject
    protected EntityManager entityManager;

    public abstract MatchViewDTO toDTO(MatchEntity entity);

    public abstract MatchEntity toEntity(MatchSaveDTO dto);

    @AfterMapping
    protected void mapRelations(MatchEntity entity, @MappingTarget MatchSaveDTO dto) {
        entity.setTeamGuest(entityManager.getReference(TeamEntity.class, dto.getTeamGuestId()));
        entity.setTeamHost(entityManager.getReference(TeamEntity.class, dto.getTeamHostId()));
        entity.setReferee(entityManager.getReference(RefereeEntity.class, dto.getRefereeId()));
        entity.setCity(entityManager.getReference(CityEntity.class, dto.getCityId()));
    }
}
