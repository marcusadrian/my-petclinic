package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.PetType;
import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.dto.PetDto;
import org.adrian.mypetclinic.dto.VisitDto;
import org.adrian.mypetclinic.repo.PetTypeRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

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
            dto.setVisits(VisitTransformers.toDto().apply(
                    pet.getVisits(),
                    Comparator.comparing(VisitDto::getDate).reversed()));
        });
    }

    public static GenericTransformer<PetDto, Pet> toEntity(PetTypeRepository petTypeRepository) {
        return new GenericTransformer<>(Pet::new, (dto, pet) -> {
            pet.setName(dto.getName());
            pet.setBirthDate(dto.getBirthDate());
            pet.setType(petTypeRepository.findByName(dto.getType()).get());
        });
    }

    public static String toCommaSeparatedString(Collection<Pet> pets) {
        return pets.stream()
                .map(Pet::getName)
                .sorted(Comparator.comparing(String::toLowerCase))
                .collect(Collectors.joining(", "));
    }


}
