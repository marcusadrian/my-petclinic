package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.PetType;
import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.dto.PetDto;

import java.util.Comparator;
import java.util.List;

public class OwnerTransformers {

    public static GenericTransformer<Owner, OwnerSummaryDto> toSummaryDto() {
        return new GenericTransformer<>(OwnerSummaryDto::new, (owner, dto) -> {
            dto.setId(owner.getId());
            dto.setName(String.format("%s %s", owner.getFirstName(), owner.getLastName()));
            dto.setAddress(owner.getAddress());
            dto.setCity(owner.getCity());
            dto.setTelephone(owner.getTelephone());
            dto.setPetNames(PetTransformers.toCommaSeparatedString(owner.getPets()));
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
            dto.setPets(PetTransformers.toDto().apply(
                    owner.getPets(),
                    Comparator.comparing(PetDto::getName)));
            dto.setPetTypes(PetTypeTransformers.toDto().apply(petTypes));
        });
    }

}
