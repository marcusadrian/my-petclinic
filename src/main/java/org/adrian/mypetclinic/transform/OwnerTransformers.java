package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.OwnerEditDto;
import org.adrian.mypetclinic.dto.OwnerSummaryDto;
import org.adrian.mypetclinic.dto.PetDto;

import java.util.Comparator;
import java.util.function.BiConsumer;

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

    public static GenericTransformer<Owner, OwnerDetailDto> toDetailDto() {
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
        });
    }

    public static BiConsumer<OwnerEditDto, Owner> updateOwner() {
        return (dto, owner) -> {
            owner.setFirstName(dto.getFirstName());
            owner.setLastName(dto.getLastName());
            owner.setAddress(dto.getAddress());
            owner.setCity(dto.getCity());
            owner.setTelephone(dto.getTelephone());
        };
    }

    public static GenericTransformer<OwnerEditDto, Owner> toOwner() {
        return new GenericTransformer<>(Owner::new, updateOwner());
    }

}
