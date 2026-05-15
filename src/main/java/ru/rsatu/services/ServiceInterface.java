package ru.rsatu.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface ServiceInterface<ViewDTO, SaveDTO, Entity> {
    ViewDTO getById(Long id);

    List<ViewDTO> getAll();

    ViewDTO create(SaveDTO bo);

    ViewDTO update(SaveDTO bo);

    void deleteById(Long id);
}
