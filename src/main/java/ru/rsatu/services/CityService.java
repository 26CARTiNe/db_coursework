package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ru.rsatu.entity.CityEntity;
import ru.rsatu.repository.CityRepository;
import ru.rsatu.dto.CityDTO;

@ApplicationScoped
public class CityService implements ICityService {

    @Inject
    CityRepository cityRepository;

    public CityDTO getById(Long id) {
        CityEntity entity = cityRepository.findById(id);

        if (entity == null) {
            throw new NotFoundException("City with id = " + id + " not found");
        }

        return toDTO(entity);
    }

    public CityDTO toDTO(CityEntity entity){
        CityDTO dto = new CityDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());
        return dto;
    }

    public CityEntity toEntity(CityDTO dto) {
        if (dto == null) {
            return null;
        }

        CityEntity entity = new CityEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCountry(dto.getCountry());
        return entity;
    }

    public List<CityDTO> getAll() {
        return cityRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public CityDTO create(CityDTO dto) {
        dto.setId(null);
        CityEntity entity = toEntity(dto);

        cityRepository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public CityDTO update(CityDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }

        CityEntity entity = cityRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("City with id = " + dto.getId() + " not found");
        }

        entity.setName(dto.getName());
        entity.setCountry(dto.getCountry());
        cityRepository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }
}
