package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.VisitEditDto;
import org.adrian.mypetclinic.service.PetService;
import org.adrian.mypetclinic.service.VisitService;
import org.adrian.mypetclinic.transform.PetTransformers;
import org.adrian.mypetclinic.transform.VisitTransformers;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 * @author Marcus Adrian
 */
@RestController
@RequestMapping("/owners/{ownerId}/pets/{petId}/visits")
public class VisitController {

    private final VisitService visitService;
    private final PetService petService;

    VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    @GetMapping("/new")
    ResponseEntity<VisitEditDto> getVisitEditDto(@PathVariable("ownerId") Long ownerId,
                                                 @PathVariable("petId") Long petId) {
        return ResponseEntity.ok(this.visitService.newVisit(ownerId, petId, VisitTransformers.toEditDto()));
    }

    @GetMapping("/{visitId}")
    ResponseEntity<VisitEditDto> getVisitEditDto(@PathVariable("ownerId") Long ownerId,
                                                 @PathVariable("petId") Long petId,
                                                 @PathVariable("visitId") Long visitId) {
        VisitEditDto visit = this.visitService.findVisit(ownerId, petId, visitId, VisitTransformers.toEditDto()).get();
        return ResponseEntity.ok(visit);
    }

    @PutMapping
    ResponseEntity<Void> addVisit(@PathVariable("ownerId") Long ownerId,
                                  @PathVariable("petId") Long petId,
                                  @Valid @RequestBody VisitEditDto visit) {
        Assert.isTrue(visit.getPet().getId().equals(petId), "Pet id in path differs from pet id in payload.");
        this.petService.updatePet(ownerId, petId, visit, PetTransformers.addVisit());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{visitId}")
    ResponseEntity<Void> updateVisit(@PathVariable("ownerId") Long ownerId,
                                     @PathVariable("petId") Long petId,
                                     @PathVariable("visitId") Long visitId,
                                     @Valid @RequestBody VisitEditDto visit) {
        Assert.isTrue(visit.getPet().getId().equals(petId), "Pet id in path differs from pet id in payload.");
        Assert.isTrue(visit.getVisit().getId().equals(visitId), "Visit id in path differs from visit id in payload.");
        this.visitService.updateVisit(ownerId, petId, visitId, visit, VisitTransformers.updateVisit());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{visitId}")
    ResponseEntity<Void> deleteVisit(@PathVariable("ownerId") Long ownerId,
                                     @PathVariable("petId") Long petId,
                                     @PathVariable("visitId") Long visitId) {
        this.visitService.deleteVisit(ownerId, petId, visitId);
        return ResponseEntity.noContent().build();
    }


}
