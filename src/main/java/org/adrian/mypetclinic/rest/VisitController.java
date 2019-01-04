package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.service.ViewAdapterService;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 * @author Marcus Adrian
 *
 */
@RequestMapping("/owners/{ownerId}/pets/{petId}/visits")
public class VisitController {

    private final ViewAdapterService service;

    VisitController(ViewAdapterService service) {
        this.service = service;
    }

}
