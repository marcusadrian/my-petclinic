package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.VisitDto;
import org.adrian.mypetclinic.dto.VisitEditDto;
import org.adrian.mypetclinic.service.VisitService;
import org.adrian.mypetclinic.transform.VisitTransformers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/new")
    ResponseEntity<VisitEditDto> newVisit(@PathVariable("ownerId") Long ownerId,
                                          @PathVariable("petId") Long petId) {
        return ResponseEntity.ok(this.visitService.newVisit(ownerId, petId, VisitTransformers.toEditDto()));
    }

    @GetMapping("/{visitId}")
    ResponseEntity<VisitDto> findVisit(@PathVariable("ownerId") Long ownerId,
                                       @PathVariable("petId") Long petId,
                                       @PathVariable("visitId") Long visitId) {
        VisitDto visit = this.visitService.findVisit(ownerId, petId, visitId, VisitTransformers.toDto()).get();
        return ResponseEntity.ok(visit);
    }

    @PutMapping
    ResponseEntity<Void> createVisit(@PathVariable("ownerId") Long ownerId,
                                     @PathVariable("petId") Long petId,
                                     @Valid @RequestBody VisitEditDto visit) {

        this.visitService.createVisit(ownerId, petId, visit, VisitTransformers.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{visitId}")
    ResponseEntity<Void> updateVisit(@PathVariable("ownerId") Long ownerId,
                                     @PathVariable("petId") Long petId,
                                     @PathVariable("visitId") Long visitId,
                                     @Valid @RequestBody VisitEditDto visit) {

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
