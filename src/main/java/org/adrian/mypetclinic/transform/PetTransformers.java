package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.PetType;
import org.adrian.mypetclinic.dto.PetDto;

import java.util.Optional;

public class PetTransformers {
    public static GenericTransformer<Pet, PetDto> toDto() {
        return new GenericTransformer<>(PetDto::new, (pet, dto) -> {
            dto.setId(pet.getId());
            dto.setName(pet.getName());
            dto.setBirthDate(pet.getBirthDate());
            dto.setType(Optional
                    .ofNullable(pet.getType())
                    .map(PetType::getName)
                    .orElse(null));
        });
    }

}
