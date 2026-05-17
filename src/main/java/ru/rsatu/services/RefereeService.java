package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import ru.rsatu.db.entity.CityEntity;
import ru.rsatu.db.entity.RefereeEntity;
import ru.rsatu.mapper.RefereeMapper;
import ru.rsatu.repository.RefereeRepository;
import ru.rsatu.dto.view.RefereeViewDTO;
import ru.rsatu.dto.save.RefereeSaveDTO;

@ApplicationScoped
public class RefereeService implements ServiceInterface<RefereeViewDTO, RefereeSaveDTO, RefereeEntity> {

    private final RefereeRepository refereeRepository;
    private final RefereeMapper refereeMapper;
    private final EntityManager entityManager;

    @Inject
    RefereeService(RefereeRepository refereeRepository,
            RefereeMapper refereeMapper,
            EntityManager entityManager) {
        this.refereeRepository = refereeRepository;
        this.refereeMapper = refereeMapper;
        this.entityManager = entityManager;
    }

    public RefereeViewDTO getById(Long id) {
        RefereeEntity entity = refereeRepository.findById(id);

        if (entity == null) {
            throw new NotFoundException("Referee with id = " + id + " not found");
        }

        return refereeMapper.toDTO(entity);
    }

    public List<RefereeViewDTO> getAll() {
        return refereeRepository.findAll().stream().map(refereeMapper::toDTO).toList();
    }

    @Transactional
    public RefereeViewDTO create(RefereeSaveDTO dto) {
        dto.setId(null);
        RefereeEntity entity = refereeMapper.toEntity(dto);

        refereeRepository.save(entity);
        return refereeMapper.toDTO(entity);
    }

    @Transactional
    public RefereeViewDTO update(RefereeSaveDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }

        RefereeEntity entity = refereeRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("Referee with id = " + dto.getId() + " not found");
        }

        entity.setCity(entityManager.getReference(CityEntity.class, dto.getCityId()));

        refereeRepository.save(entity);
        return refereeMapper.toDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        refereeRepository.deleteById(id);
    }
}
