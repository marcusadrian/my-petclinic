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
import org.adrian.mypetclinic.dto.OwnerEditDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.predicate.OwnerSearchPredicates;
import org.adrian.mypetclinic.service.OwnerSearchCriteria;
import org.adrian.mypetclinic.service.OwnerService;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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

    private final OwnerService service;

    OwnerController(OwnerService service) {
        this.service = service;
    }

    @GetMapping(value = "/{ownerId}", produces = MediaTypes.HAL_JSON_VALUE)
    Resource<OwnerDetailDto> findById(@PathVariable("ownerId") Long ownerId) {
        OwnerDetailDto owner = this.service.findById(ownerId, OwnerTransformers.toDetailDto()).get();
        Link link = linkTo(
                methodOn(OwnerController.class)
                        .findById(owner.getId()))
                .withSelfRel();
        return new Resource<>(owner, link);
    }

    @GetMapping(value = "/search", produces = MediaTypes.HAL_JSON_VALUE)
    PagedResources<Resource<OwnerSummaryDto>> findOwners(OwnerSearchCriteria criteria,
                                                         Pageable pageable,
                                                         PagedResourcesAssembler<OwnerSummaryDto> pagedResourcesAssembler) {

        Page<OwnerSummaryDto> page = this.service.findOwners(
                OwnerSearchPredicates.ownerSearch(criteria),
                OwnerTransformers.toSummaryDto(),
                pageable);

        ResourceAssembler<OwnerSummaryDto, Resource<OwnerSummaryDto>> resourceAssembler = owner ->
                new Resource<>(owner,
                        linkTo(methodOn(OwnerController.class).findById(owner.getId())).withRel("owner"));

        Link selfLink = linkTo(methodOn(OwnerController.class)
                .findOwners(criteria, pageable, pagedResourcesAssembler))
                .withSelfRel();

        return pagedResourcesAssembler.toResource(page, resourceAssembler, selfLink);

    }

    @PostMapping("/{ownerId}")
    ResponseEntity<Void> updateOwner(@Valid @RequestBody OwnerEditDto owner, @PathVariable("ownerId") Long ownerId) {
        owner.setId(ownerId);
        this.service.updateOwner(owner.getId(), owner, OwnerTransformers.updateOwner());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    ResponseEntity<OwnerDetailDto> createOwner(@Valid @RequestBody OwnerEditDto owner) {
        Long id = this.service.createOwner(owner, OwnerTransformers.toOwner());
        OwnerDetailDto createdOwner = this.service.findById(id, OwnerTransformers.toDetailDto()).get();
        URI location = linkTo(OwnerController.class).slash(createdOwner).toUri();
        return ResponseEntity.created(location).body(createdOwner);
    }

    @DeleteMapping("/{ownerId}")
    ResponseEntity<Void> deleteOwner(@PathVariable("ownerId") Long ownerId) {
        this.service.deleteOwner(ownerId);
        return ResponseEntity.noContent().build();
    }

}
