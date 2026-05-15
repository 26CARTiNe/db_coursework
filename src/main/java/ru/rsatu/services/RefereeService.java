package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import ru.rsatu.db.entity.RefereeEntity;
import ru.rsatu.mapper.RefereeMapper;
import ru.rsatu.repository.RefereeRepository;
import ru.rsatu.dto.view.RefereeViewDTO;
import ru.rsatu.dto.save.RefereeSaveDTO;

@ApplicationScoped
public class RefereeService implements ServiceInterface<RefereeViewDTO, RefereeSaveDTO, RefereeEntity> {

    private final RefereeRepository refereeRepository;
    private final RefereeMapper refereeMapper;

    @Inject
    RefereeService(RefereeRepository refereeRepository,
            RefereeMapper refereeMapper) {
        this.refereeRepository = refereeRepository;
        this.refereeMapper = refereeMapper;
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

    public RefereeViewDTO create(RefereeSaveDTO dto) {
        RefereeEntity entity = refereeMapper.toEntity(dto);

        refereeRepository.save(entity);
        return refereeMapper.toDTO(entity);
    }

    public RefereeViewDTO update(RefereeSaveDTO dto) {
        RefereeEntity entity = refereeRepository.findById(dto.getId());

        if (entity == null) {
            throw new NotFoundException("Referee with id = " + dto.getId() + " not found");
        }

        entity.setCity(dto.getCityId());

        refereeRepository.save(entity);
        return refereeMapper.toDTO(entity);
    }

    public void deleteById(Long id) {
        refereeRepository.deleteById(id);
    }
}
