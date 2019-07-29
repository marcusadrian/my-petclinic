package org.adrian.mypetclinic.rest;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @GetMapping("/new")
    ResponseEntity<VisitEditDto> prepareNew(@PathVariable("ownerId") Long ownerId,
                                            @PathVariable("petId") Long petId) {
        return ResponseEntity.ok(this.visitService.newVisit(ownerId, petId, VisitTransformers.toEditDto()));
    }

    @GetMapping("/{visitId}/edit")
    ResponseEntity<VisitEditDto> prepareEdit(@PathVariable("ownerId") Long ownerId,
                                             @PathVariable("petId") Long petId,
                                             @PathVariable("visitId") Long visitId) {
        VisitEditDto visit = this.visitService.findVisit(ownerId, petId, visitId, VisitTransformers.toEditDto()).get();
        return ResponseEntity.ok(visit);
    }

    @PostMapping
    ResponseEntity<Void> create(@PathVariable("ownerId") Long ownerId,
                                @PathVariable("petId") Long petId,
                                @Valid @RequestBody VisitEditDto visit) {

        this.visitService.createVisit(ownerId, petId, visit, VisitTransformers.toVisit());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{visitId}")
    ResponseEntity<Void> update(@PathVariable("ownerId") Long ownerId,
                                @PathVariable("petId") Long petId,
                                @PathVariable("visitId") Long visitId,
                                @Valid @RequestBody VisitEditDto visit) {

        this.visitService.updateVisit(ownerId, petId, visitId, visit, VisitTransformers.toVisit());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{visitId}")
    ResponseEntity<Void> delete(@PathVariable("ownerId") Long ownerId,
                                @PathVariable("petId") Long petId,
                                @PathVariable("visitId") Long visitId) {
        this.visitService.deleteVisit(ownerId, petId, visitId);
        return ResponseEntity.noContent().build();
    }


}
