package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.PetEditDto;
import org.adrian.mypetclinic.repo.PetTypeRepository;
import org.adrian.mypetclinic.service.PetService;
import org.adrian.mypetclinic.transform.PetTransformers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<PetEditDto> prepareNew(@PathVariable("ownerId") Long ownerId) {
        return ResponseEntity.ok(this.service.newPet(ownerId, PetTransformers.toEditDto(this.petTypeRepository)));
    }

    @GetMapping("/{petId}/edit")
    ResponseEntity<PetEditDto> prepareEdit(@PathVariable("ownerId") Long ownerId, @PathVariable("petId") Long petId) {
        PetEditDto pet = this.service
                .findPet(ownerId, petId, PetTransformers.toEditDto(this.petTypeRepository))
                .get();
        return ResponseEntity.ok(pet);
    }

    @PutMapping
    ResponseEntity<Void> create(@PathVariable("ownerId") Long ownerId, @Valid @RequestBody PetEditDto pet) {
        this.service.createPet(ownerId, pet, PetTransformers.toPet(this.petTypeRepository));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{petId}")
    ResponseEntity<Void> update(@PathVariable("ownerId") Long ownerId,
                                @PathVariable("petId") Long petId,
                                @Valid @RequestBody PetEditDto pet) {
        this.service.updatePet(ownerId, petId, pet, PetTransformers.toPet(this.petTypeRepository));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{petId}")
    ResponseEntity<Void> deletePet(@PathVariable("ownerId") Long ownerId,
                                   @PathVariable("petId") Long petId) {
        this.service.deletePet(ownerId, petId);
        return ResponseEntity.noContent().build();
    }

}
