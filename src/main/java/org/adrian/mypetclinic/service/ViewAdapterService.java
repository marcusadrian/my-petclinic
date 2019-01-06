package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.dto.*;
import org.adrian.mypetclinic.predicate.OwnerSearchPredicates;
import org.adrian.mypetclinic.repo.PetTypeRepository;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.adrian.mypetclinic.transform.PetTransformers;
import org.adrian.mypetclinic.transform.VisitTransformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ViewAdapterService {

    private final OwnerService ownerService;
    private final PetService petService;
    private final VisitService visitService;
    private final PetTypeRepository petTypeRepository;

    public ViewAdapterService(OwnerService ownerService, PetService petService, VisitService visitService, PetTypeRepository petTypeRepository) {
        this.ownerService = ownerService;
        this.petService = petService;
        this.visitService = visitService;
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

    public void addPet(Long ownerId, PetEditDto pet) {
        this.ownerService.updateOwner(ownerId, pet, OwnerTransformers.addPet(this.petTypeRepository));
    }

    public void updatePet(Long ownerId, PetEditDto pet) {
        this.petService.updatePet(ownerId, pet.getId(), pet, PetTransformers.updatePet(this.petTypeRepository));
    }

    public void addVisit(Long ownerId, Long petId, VisitEditDto visit) {
        this.petService.updatePet(ownerId, petId, visit, PetTransformers.addVisit());
    }

    public void updateVisit(Long ownerId, Long petId, Long visitId, VisitEditDto visit) {
        this.visitService.updateVisit(ownerId, petId, visitId, visit, VisitTransformers.updateVisit());
    }

    public Optional<PetEditDto> findPetEditDto(Long ownerId, Long petId) {
        return this.petService.findPet(ownerId, petId, PetTransformers.toEditDto(this.petTypeRepository));
    }

    public PetEditDto newPetEditDto(Long ownerId) {
        return this.petService.newPet(ownerId, PetTransformers.toEditDto(this.petTypeRepository));
    }

    public Optional<VisitEditDto> findVisitEditDto(Long ownerId, Long petId, Long visitId) {
        return this.visitService.findVisit(ownerId, petId, visitId, VisitTransformers.toEditDto());
    }

    public VisitEditDto newVisitEditDto(Long ownerId, Long petId) {
        return this.visitService.newVisit(ownerId, petId, VisitTransformers.toEditDto());
    }

    public OwnerDetailDto createOwner(OwnerEditDto owner) {
        Long id = this.ownerService.createOwner(owner, OwnerTransformers.toOwner());
        return this.ownerService.findById(id, OwnerTransformers.toDetailDto()).get();
    }

    public void deletePet(Long ownerId, Long petId) {
        this.ownerService.updateOwner(ownerId, petId, OwnerTransformers.deletePet());
    }

    public void deleteVisit(Long ownerId, Long petId, Long visitId) {
        this.petService.updatePet(ownerId, petId, visitId, PetTransformers.deleteVisit());
    }
}
