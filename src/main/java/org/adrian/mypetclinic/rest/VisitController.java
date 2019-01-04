package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.VisitEditDto;
import org.adrian.mypetclinic.service.ViewAdapterService;
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

    private final ViewAdapterService service;

    VisitController(ViewAdapterService service) {
        this.service = service;
    }

    @GetMapping("/new")
    ResponseEntity<VisitEditDto> getVisitEditDto(@PathVariable("ownerId") Long ownerId, @PathVariable("petId") Long petId) {
        return ResponseEntity.ok(this.service.newVisitEditDto(ownerId, petId));
    }

    @PutMapping
    ResponseEntity<Void> addVisit(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("petId") Long petId,
            @Valid @RequestBody VisitEditDto visit) {
        Assert.isTrue(visit.getPet().getId().equals(petId), "Pet id in path differs from pet id in payload.");
        this.service.addVisit(ownerId, visit);
        return ResponseEntity.noContent().build();
    }


}
