package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerEditDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.dto.PetDto;
import org.adrian.mypetclinic.predicate.OwnerSearchPredicates;
import org.adrian.mypetclinic.repo.PetTypeRepository;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ViewAdapterService {

    private final OwnerService ownerService;
    private final PetTypeRepository petTypeRepository;

    public ViewAdapterService(OwnerService ownerService, PetTypeRepository petTypeRepository) {
        this.ownerService = ownerService;
        this.petTypeRepository = petTypeRepository;
    }

    public Page<OwnerSummaryDto> findOwnersByCriteria(
            OwnerSearchCriteria criteria, Pageable pageable) {

        return this.ownerService.findOwners(
                OwnerSearchPredicates.ownerSearch(criteria),
                OwnerTransformers.toSummaryDto(),
                pageable);
    }

    public Optional<OwnerDetailDto> findOwnerDetailDtoById(Long id) {
        return this.ownerService.findById(id, OwnerTransformers.toDetailDto());
    }

    public void updateOwner(OwnerEditDto owner) {
        this.ownerService.updateOwner(owner.getId(), owner, OwnerTransformers.updateOwner());
    }

    public void addPet(Long ownerId, PetDto pet) {
        this.ownerService.updateOwner(ownerId, pet, OwnerTransformers.addPet(this.petTypeRepository));
    }

    public void updatePet(Long ownerId, PetDto pet) {
        this.ownerService.updateOwner(ownerId, pet, OwnerTransformers.updatePet(this.petTypeRepository));
    }

    public void createOwner(OwnerEditDto owner) {
        this.ownerService.createOwner(owner, OwnerTransformers.toOwner());
    }

}
