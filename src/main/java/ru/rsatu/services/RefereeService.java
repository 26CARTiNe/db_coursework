package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import ru.rsatu.entity.CityEntity;
import ru.rsatu.entity.RefereeEntity;
import ru.rsatu.dto.RefereeDTO;
import ru.rsatu.repository.CityRepository;
import ru.rsatu.repository.RefereeRepository;

@ApplicationScoped
public class RefereeService implements IRefereeService {

    @Inject
    RefereeRepository refereeRepository;
    @Inject
    ICityService cityService;

    public RefereeDTO getById(Long id) {
        RefereeEntity entity = refereeRepository.findById(id);

        if (entity == null) {
            throw new NotFoundException("Referee with id = " + id + " not found");
        }

        return toDTO(entity);
    }

    @Override
    public RefereeDTO toDTO(RefereeEntity entity) {
        if (entity == null) {
            return null;
        }

        RefereeDTO dto = new RefereeDTO();
        dto.setId(entity.getId());
        dto.setCity(cityService.toDTO(entity.getCity()));
        dto.setFIO(entity.getFIO());
        dto.setLicense(entity.getLicense());
        dto.setStageYears(entity.getStageYears());
        return dto;
    }

    @Override
    public RefereeEntity toEntity(RefereeDTO dto) {
        if (dto == null) {
            return null;
        }

        RefereeEntity entity = new RefereeEntity();
        entity.setId(dto.getId());

        if (dto.getCity() != null && dto.getCity().getId() != null) {
            entity.setCity(cityService.toEntity(dto.getCity()));
        }

        entity.setFIO(dto.getFIO());
        entity.setLicense(dto.getLicense());
        entity.setStageYears(dto.getStageYears());
        return entity;
    }

    public List<RefereeDTO> getAll() {
        return refereeRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public RefereeDTO create(RefereeDTO dto) {
        dto.setId(null);
        RefereeEntity entity = toEntity(dto);

        refereeRepository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public RefereeDTO update(RefereeDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }

        RefereeEntity entity = refereeRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("Referee with id = " + dto.getId() + " not found");
        }

        refereeRepository.save(toEntity(dto));
        return toDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        refereeRepository.deleteById(id);
    }
}