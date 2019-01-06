package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.PetEditDto;
import org.adrian.mypetclinic.repo.PetTypeRepository;
import org.adrian.mypetclinic.service.PetService;
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

    private final PetService service;
    private final PetTypeRepository petTypeRepository;

    PetController(PetService service, PetTypeRepository petTypeRepository) {
        this.service = service;
        this.petTypeRepository = petTypeRepository;
    }

    @GetMapping("/new")
    ResponseEntity<PetEditDto> getPetEditDto(@PathVariable("ownerId") Long ownerId) {
        return ResponseEntity.ok(this.service.newPet(ownerId, PetTransformers.toEditDto(this.petTypeRepository)));
    }

    @GetMapping("/{petId}")
    ResponseEntity<PetEditDto> getPetEditDto(@PathVariable("ownerId") Long ownerId, @PathVariable("petId") Long petId) {
        PetEditDto pet = this.service
                .findPet(ownerId, petId, PetTransformers.toEditDto(this.petTypeRepository))
                .get();
        return ResponseEntity.ok(pet);
    }

    @PutMapping
    ResponseEntity<Void> addPet(@PathVariable("ownerId") Long ownerId, @Valid @RequestBody PetEditDto pet) {
        this.service.createPet(ownerId, pet, PetTransformers.toEntity(this.petTypeRepository));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{petId}")
    ResponseEntity<Void> updatePet(@PathVariable("ownerId") Long ownerId,
                                   @PathVariable("petId") Long petId,
                                   @Valid @RequestBody PetEditDto pet) {
        Assert.isTrue(pet.getId().equals(petId), "Pet id in path differs from pet id in payload.");
        this.service.updatePet(ownerId, pet.getId(), pet, PetTransformers.updatePet(this.petTypeRepository));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{petId}")
    ResponseEntity<Void> deletePet(@PathVariable("ownerId") Long ownerId,
                                   @PathVariable("petId") Long petId) {
        this.service.deletePet(ownerId, petId);
        return ResponseEntity.noContent().build();
    }

}
