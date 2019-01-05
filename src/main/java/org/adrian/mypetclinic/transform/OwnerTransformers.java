package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.dto.*;
import org.adrian.mypetclinic.repo.PetTypeRepository;

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

    public static BiConsumer<PetEditDto, Owner> addPet(PetTypeRepository petTypeRepository) {
        return (pet, owner) ->
                owner.addPet(PetTransformers.toEntity(petTypeRepository).apply(pet));
    }

    public static BiConsumer<Long, Owner> deletePet() {
        return (petId, owner) -> {
            Pet found = owner.getPets()
                    .stream()
                    .filter(pet -> pet.getId().equals(petId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("no pet with id=%s", petId)));
            owner.getPets().remove(found);
        };
    }

    public static BiConsumer<PetEditDto, Owner> updatePet(PetTypeRepository petTypeRepository) {
        return (petDto, owner) -> {
            Pet pet = owner.getPets()
                    .stream()
                    .filter(entity -> entity.getId().equals(petDto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("no pet with id=%s", petDto.getId())));
            PetTransformers.updatePet(petTypeRepository).accept(petDto, pet);
        };
    }

    public static BiConsumer<VisitEditDto, Owner> addVisit() {
        return (visit, owner) -> {
            Pet pet = owner.getPets().stream()
                    .filter(p -> p.getId().equals(visit.getPet().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("no pet with id=%s", visit.getPet().getId())));
            pet.addVisit(VisitTransformers.toEntity().apply(visit));

        };
    }

    public static BiConsumer<VisitEditDto, Owner> updateVisit() {
        return (visitDto, owner) -> {
            Pet pet = owner.getPets().stream()
                    .filter(p -> p.getId().equals(visitDto.getPet().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("no pet with id=%s", visitDto.getPet().getId())));
            Visit visit = pet.getVisits().stream()
                    .filter(v -> v.getId().equals(visitDto.getVisit().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("no visit with id=%s", visitDto.getVisit().getId())));
            VisitTransformers.updateVisit().accept(visitDto, visit);
        };
    }

    public static GenericTransformer<OwnerEditDto, Owner> toOwner() {
        return new GenericTransformer<>(Owner::new, updateOwner());
    }

}
