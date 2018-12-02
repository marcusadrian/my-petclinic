package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.repo.PetTypeRepository;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class ViewAdapterService {

    private final OwnerService ownerService;
    private final PetTypeRepository petTypeRepository;

    public ViewAdapterService(OwnerService ownerService, PetTypeRepository petTypeRepository) {
        this.ownerService = ownerService;
        this.petTypeRepository = petTypeRepository;
    }

    public Page<Resource<OwnerSummaryDto>> findOwnerSummaryDtosByLastNameStartingWith(
            String lastName, Pageable pageable, Function<OwnerSummaryDto, Resource<OwnerSummaryDto>> toResource) {

        return ownerService.findByLastNameStartingWith(
                lastName,
                OwnerTransformers.toSummaryDto().andThen(toResource),
                pageable);
    }

    public Optional<OwnerDetailDto> findOwnerDetailDtoById(Long id) {
        return ownerService.findById(id, OwnerTransformers.toDetailDto(petTypeRepository));
    }

}
