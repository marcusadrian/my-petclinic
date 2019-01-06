package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.PetType;
import org.adrian.mypetclinic.dto.PetDto;
import org.adrian.mypetclinic.dto.PetEditDto;
import org.adrian.mypetclinic.dto.VisitDto;
import org.adrian.mypetclinic.dto.VisitEditDto;
import org.adrian.mypetclinic.repo.PetTypeRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;
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

    public static GenericTransformer<Pet, PetEditDto> toEditDto(PetTypeRepository petTypeRepository) {
        return new GenericTransformer<>(PetEditDto::new, (pet, dto) -> {
            dto.setId(pet.getId());
            dto.setName(pet.getName());
            dto.setBirthDate(pet.getBirthDate());
            dto.setType(pet.getType());
            dto.setPetTypes(PetTypeTransformers.toDto().apply(petTypeRepository.findAll()));
        });
    }

    public static GenericTransformer<PetEditDto, Pet> toEntity(PetTypeRepository petTypeRepository) {
        return new GenericTransformer<>(Pet::new, updatePet(petTypeRepository));
    }

    public static BiConsumer<PetEditDto, Pet> updatePet(PetTypeRepository petTypeRepository) {
        return (dto, pet) -> {
            pet.setName(dto.getName());
            pet.setBirthDate(dto.getBirthDate());
            pet.setType(petTypeRepository.findById(dto.getType().getId()).get());
        };
    }

    public static BiConsumer<VisitEditDto, Pet> addVisit() {
        return (visit, pet) -> pet.addVisit(VisitTransformers.toEntity().apply(visit));
    }

    public static String toCommaSeparatedString(Collection<Pet> pets) {
        return pets.stream()
                .map(Pet::getName)
                .sorted(Comparator.comparing(String::toLowerCase))
                .collect(Collectors.joining(", "));
    }

}
