package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.repo.PetTypeRepository;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ViewAdapterService {

    private final OwnerService ownerService;
    private final PetTypeRepository petTypeRepository;

    public ViewAdapterService(OwnerService ownerService, PetTypeRepository petTypeRepository) {
        this.ownerService = ownerService;
        this.petTypeRepository = petTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<OwnerSummaryDto> findOwnerSummaryDtosByLastNameStartingWith(String lastName) {
        return ownerService.findByLastNameStartingWith(lastName, OwnerTransformers.toSummaryDto());
    }

    @Transactional(readOnly = true)
    public Optional<OwnerDetailDto> findOwnerDetailDtosById(Long id) {
        // TODO sort
        return ownerService.findById(id, OwnerTransformers.toDetailDto(petTypeRepository.findAll()));
    }


}
