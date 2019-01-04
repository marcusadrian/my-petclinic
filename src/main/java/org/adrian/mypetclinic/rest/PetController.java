package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.PetEditDto;
import org.adrian.mypetclinic.service.ViewAdapterService;
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
@RequestMapping("/owners/{ownerId}")
class PetController {
    private final ViewAdapterService service;

    PetController(ViewAdapterService service) {
        this.service = service;
    }

    @GetMapping("/pets/new")
    ResponseEntity<PetEditDto> getPetEditDto(@PathVariable("ownerId") Long ownerId) {
        return ResponseEntity.ok(this.service.newPetEditDto(ownerId));
    }

    @GetMapping("/pets/{petId}")
    ResponseEntity<PetEditDto> getPetEditDto(@PathVariable("ownerId") Long ownerId, @PathVariable("petId") Long petId) {
        return ResponseEntity.ok(this.service.findPetEditDto(ownerId, petId).get());
    }

    @PutMapping("/pets")
    ResponseEntity<Void> addPet(@PathVariable("ownerId") Long ownerId, @Valid @RequestBody PetEditDto pet) {
        this.service.addPet(ownerId, pet);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pets/{petId}")
    ResponseEntity<Void> updatePet(@PathVariable("ownerId") Long ownerId,
                                   @PathVariable("petId") Long petId,
                                   @Valid @RequestBody PetEditDto pet) {
        Assert.isTrue(pet.getId().equals(petId),"Pet id in path differs from pet id in payload.");
        this.service.updatePet(ownerId, pet);
        return ResponseEntity.noContent().build();
    }
}
