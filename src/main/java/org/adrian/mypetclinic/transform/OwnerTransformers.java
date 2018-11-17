package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.dto.OwnerDto;

import java.util.HashSet;

public class OwnerTransformers {

    public static GenericTransformer<Owner, OwnerDto> toDto() {
        return new GenericTransformer<>(OwnerDto::new, (owner, dto) -> {
            dto.setFirstName(owner.getFirstName());
            dto.setLastName(owner.getLastName());
            dto.setAddress(owner.getAddress());
            dto.setCity(owner.getCity());
            dto.setTelephone(owner.getTelephone());
            dto.setPets(PetTransformers.toDto().apply(owner.getPets(), new HashSet<>()));
        });
    }
}
