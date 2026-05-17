package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ru.rsatu.db.entity.CityEntity;
import ru.rsatu.mapper.CityMapper;
import ru.rsatu.repository.CityRepository;
import ru.rsatu.dto.view.CityViewDTO;
import ru.rsatu.dto.save.CitySaveDTO;

@ApplicationScoped
public class CityService implements ServiceInterface<CityViewDTO, CitySaveDTO, CityEntity> {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Inject
    CityService(CityRepository cityRepository,
            CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public CityViewDTO getById(Long id) {
        CityEntity entity = cityRepository.findById(id);

        if (entity == null) {
            throw new NotFoundException("City with id = " + id + " not found");
        }

        return cityMapper.toDTO(entity);
    }

    public List<CityViewDTO> getAll() {
        return cityRepository.findAll().stream().map(cityMapper::toDTO).toList();
    }

    @Transactional
    public CityViewDTO create(CitySaveDTO dto) {
        dto.setId(null);
        CityEntity entity = cityMapper.toEntity(dto);

        cityRepository.save(entity);
        return cityMapper.toDTO(entity);
    }

    @Transactional
    public CityViewDTO update(CitySaveDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Id is required for update");
        }

        CityEntity entity = cityRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("City with id = " + dto.getId() + " not found");
        }

        cityRepository.save(entity);
        return cityMapper.toDTO(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }
}
