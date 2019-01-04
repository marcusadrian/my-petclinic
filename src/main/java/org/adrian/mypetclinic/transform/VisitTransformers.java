package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.dto.VisitDto;
import org.adrian.mypetclinic.dto.VisitEditDto;

import java.util.function.BiConsumer;

public class VisitTransformers {
    public static GenericTransformer<Visit, VisitDto> toDto() {
        return new GenericTransformer<>(VisitDto::new, (visit, dto) -> {
            dto.setId(visit.getId());
            dto.setDate(visit.getDate());
            dto.setDescription(visit.getDescription());
        });
    }

    public static GenericTransformer<Visit, VisitEditDto> toEditDto() {
        return new GenericTransformer<>(VisitEditDto::new, (entity, dto) -> {
            Pet pet = entity.getPet();
            dto.setPet(PetTransformers.toDto().apply(pet));
            Owner owner = entity.getPet().getOwner();
            dto.setOwnerName(String.format("%s %s", owner.getFirstName(), owner.getLastName()));
            dto.setVisit(toDto().apply(entity));
        });
    }

    public static GenericTransformer<VisitEditDto, Visit> toEntity() {
        return new GenericTransformer<>(Visit::new, (dto, entity) -> {
            VisitDto visit = dto.getVisit();
            entity.setId(visit.getId());
            entity.setDate(visit.getDate());
            entity.setDescription(visit.getDescription());
        });
    }

    public static BiConsumer<VisitEditDto, Owner> addVisit() {
        return (visit, owner) -> {
            Pet pet = owner.getPets().stream()
                    .filter(p -> p.getId().equals(visit.getPet().getId()))
                    .findFirst()
                    .get();
            pet.addVisit(VisitTransformers.toEntity().apply(visit));

        };
    }

}
