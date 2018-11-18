package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.PetType;
import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;

import java.util.List;
import java.util.stream.Collectors;

public class OwnerTransformers {

    public static GenericTransformer<Owner, OwnerSummaryDto> toSummaryDto() {
        return new GenericTransformer<>(OwnerSummaryDto::new, (owner, dto) -> {
            dto.setId(owner.getId());
            dto.setName(String.format("%s %s", owner.getFirstName(), owner.getLastName()));
            dto.setAddress(owner.getAddress());
            dto.setCity(owner.getCity());
            dto.setTelephone(owner.getTelephone());
            String petNames = owner.getPets().stream()
                    .map(Pet::getName)
                    .collect(Collectors.joining(", "));
            dto.setPetNames(petNames);
        });
    }

    public static GenericTransformer<Owner, OwnerDetailDto> toDetailDto(List<PetType> petTypes) {
        return new GenericTransformer<>(OwnerDetailDto::new, (owner, dto) -> {
            dto.setId(owner.getId());
            dto.setFirstName(owner.getFirstName());
            dto.setLastName(owner.getLastName());
            dto.setAddress(owner.getAddress());
            dto.setCity(owner.getCity());
            dto.setTelephone(owner.getTelephone());
            dto.setPets(PetTransformers.toDto().apply(owner.getPets()));
            dto.setPetTypes(petTypes);
        });
    }

}
