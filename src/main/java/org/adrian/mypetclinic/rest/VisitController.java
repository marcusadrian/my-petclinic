package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.VisitEditDto;
import org.adrian.mypetclinic.service.ViewAdapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(this.service.findVisitEditDto(ownerId, petId).get());
    }

}
