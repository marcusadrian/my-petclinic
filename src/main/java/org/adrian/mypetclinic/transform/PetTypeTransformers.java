package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.PetType;
import org.adrian.mypetclinic.dto.PetTypeDto;

public class PetTypeTransformers {

    public static GenericTransformer<PetType, PetTypeDto> toDto() {
        return new GenericTransformer<>(PetTypeDto::new, (petType, dto) -> {
            dto.setId(petType.getId());
            dto.setName(petType.getName());
        });
    }

}
