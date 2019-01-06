package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.PetEditDto;
import org.adrian.mypetclinic.repo.PetTypeRepository;
import org.adrian.mypetclinic.service.OwnerService;
import org.adrian.mypetclinic.service.PetService;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.adrian.mypetclinic.transform.PetTransformers;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Marcus Adrian
 */
@RestController
@RequestMapping("/owners/{ownerId}/pets")
class PetController {

    private final OwnerService ownerService;
    private final PetService petService;
    private final PetTypeRepository petTypeRepository;

    PetController(OwnerService ownerService, PetService petService, PetTypeRepository petTypeRepository) {
        this.ownerService = ownerService;
        this.petService = petService;
        this.petTypeRepository = petTypeRepository;
    }

    @GetMapping("/new")
    ResponseEntity<PetEditDto> getPetEditDto(@PathVariable("ownerId") Long ownerId) {
        return ResponseEntity.ok(this.petService.newPet(ownerId, PetTransformers.toEditDto(this.petTypeRepository)));
    }

    @GetMapping("/{petId}")
    ResponseEntity<PetEditDto> getPetEditDto(@PathVariable("ownerId") Long ownerId, @PathVariable("petId") Long petId) {
        PetEditDto pet = this.petService
                .findPet(ownerId, petId, PetTransformers.toEditDto(this.petTypeRepository))
                .get();
        return ResponseEntity.ok(pet);
    }

    @PutMapping
    ResponseEntity<Void> addPet(@PathVariable("ownerId") Long ownerId, @Valid @RequestBody PetEditDto pet) {
        this.ownerService.updateOwner(ownerId, pet, OwnerTransformers.addPet(this.petTypeRepository));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{petId}")
    ResponseEntity<Void> updatePet(@PathVariable("ownerId") Long ownerId,
                                   @PathVariable("petId") Long petId,
                                   @Valid @RequestBody PetEditDto pet) {
        Assert.isTrue(pet.getId().equals(petId), "Pet id in path differs from pet id in payload.");
        this.petService.updatePet(ownerId, pet.getId(), pet, PetTransformers.updatePet(this.petTypeRepository));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{petId}")
    ResponseEntity<Void> deletePet(@PathVariable("ownerId") Long ownerId,
                                   @PathVariable("petId") Long petId) {
        this.petService.deletePet(ownerId, petId);
        return ResponseEntity.noContent().build();
    }

}
