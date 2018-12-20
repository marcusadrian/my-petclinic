package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.repo.PetTypeRepository;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.adrian.mypetclinic.util.PageRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ViewAdapterService {

    private final OwnerService ownerService;
    private final PetTypeRepository petTypeRepository;

    public ViewAdapterService(OwnerService ownerService, PetTypeRepository petTypeRepository) {
        this.ownerService = ownerService;
        this.petTypeRepository = petTypeRepository;
    }

    public Page<OwnerSummaryDto> findOwnerSummaryDtosByLastNameStartingWith(
            String lastName, Pageable pageable) {

        Page<OwnerSummaryDto> ownerPage = ownerSummaryDtosSearch(lastName, pageable);
        // if requested page doesn't exist (out of range), return the first page
        if (ownerPage.getNumber() >= ownerPage.getTotalPages()) {
            Pageable fromStartPageable = PageRequestBuilder.of(pageable).pageNumber(0).build();
            ownerPage = ownerSummaryDtosSearch(lastName, fromStartPageable);
        }
        return ownerPage;
    }

    private Page<OwnerSummaryDto> ownerSummaryDtosSearch(String lastName, Pageable pageable) {
        return ownerService.findByLastNameStartingWith(
                    lastName,
                    OwnerTransformers.toSummaryDto(),
                    pageable);
    }

    public Optional<OwnerDetailDto> findOwnerDetailDtoById(Long id) {
        return ownerService.findById(id, OwnerTransformers.toDetailDto(petTypeRepository));
    }

}
