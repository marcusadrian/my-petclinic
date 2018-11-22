/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.service.ViewAdapterService;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Marcus Adrian
 */
@RestController
@RequestMapping("/owners")
class OwnerController {

    private final ViewAdapterService service;

    OwnerController(ViewAdapterService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    Resource<OwnerDetailDto> findById(@PathVariable("id") Long id) {
        OwnerDetailDto owner = service.findOwnerDetailDtosById(id).get();
        Link link = linkTo(
                methodOn(OwnerController.class)
                        .findById(owner.getId()))
                .withSelfRel();
        return new Resource<>(owner, link);
    }

    @GetMapping("/search")
    ResponseEntity<List<OwnerSummaryDto>> findByLastName(@RequestParam("lastName") String lastName) {
        return ResponseEntity.ok(service.findOwnerSummaryDtosByLastNameStartingWith(lastName));
    }

}
