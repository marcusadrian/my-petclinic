package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerEditDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.predicate.OwnerSearchPredicates;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class ViewAdapterService {

    private final OwnerService ownerService;

    public ViewAdapterService(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public Page<OwnerSummaryDto> findOwnersByCriteria(
            OwnerSearchCriteria criteria, Pageable pageable) {

        return this.ownerService.findOwners(
                OwnerSearchPredicates.ownerSearch(criteria),
                OwnerTransformers.toSummaryDto(),
                pageable);
    }

    public Optional<OwnerDetailDto> findOwnerDetailDtoById(Long id) {
        return this.ownerService.findById(id, OwnerTransformers.toDetailDto());
    }

    public void updateOwner(OwnerEditDto owner) {
        this.ownerService.updateOwner(owner, owner.getId(), OwnerTransformers.ownerTransformedByEditDto());
    }

    public void createOwner(OwnerEditDto owner) {
        this.ownerService.createOwner(owner, OwnerTransformers.toOwner());
    }

}
