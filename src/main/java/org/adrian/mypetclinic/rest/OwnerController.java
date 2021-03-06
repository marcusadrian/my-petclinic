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

import lombok.RequiredArgsConstructor;
import org.adrian.mypetclinic.dto.BaseDto;
import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerEditDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.predicate.OwnerSearchPredicates;
import org.adrian.mypetclinic.service.OwnerSearchCriteria;
import org.adrian.mypetclinic.service.OwnerService;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Marcus Adrian
 */
@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
class OwnerController {

    private final OwnerService service;

    @GetMapping(value = "/{ownerId}")
    ResponseEntity<OwnerDetailDto> find(@PathVariable("ownerId") Long ownerId) {
       return ResponseEntity.of(this.service.findById(ownerId, OwnerTransformers.toDetailDto()));
    }

    @GetMapping(value = "/{ownerId}/edit")
    ResponseEntity<OwnerEditDto> prepareEdit(@PathVariable("ownerId") Long ownerId) {
        return ResponseEntity.of(this.service.findById(ownerId, OwnerTransformers.toEditDto()));
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<OwnerSummaryDto>> search(OwnerSearchCriteria criteria, Pageable pageable) {

        Page<OwnerSummaryDto> page = this.service.findOwners(
                OwnerSearchPredicates.ownerSearch(criteria),
                OwnerTransformers.toSummaryDto(),
                pageable);

        return ResponseEntity.ok(page);
    }

    @PutMapping("/{ownerId}")
    ResponseEntity<Void> update(@Valid @RequestBody OwnerEditDto owner, @PathVariable("ownerId") Long ownerId) {
        this.service.updateOwner(ownerId, owner, OwnerTransformers.toOwner());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    ResponseEntity<BaseDto> create(@Valid @RequestBody OwnerEditDto owner) {
        BaseDto createdOwner = this.service.createOwner(
                owner,
                OwnerTransformers.toOwner(),
                BaseDto::fromBaseEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOwner);
    }

    @DeleteMapping("/{ownerId}")
    ResponseEntity<Void> delete(@PathVariable("ownerId") Long ownerId) {
        this.service.deleteOwner(ownerId);
        return ResponseEntity.noContent().build();
    }

}
