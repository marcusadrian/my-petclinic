package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.dto.VisitDto;
import org.adrian.mypetclinic.dto.VisitEditDto;

public class VisitTransformers {
    public static GenericTransformer<Visit, VisitDto> toDto() {
        return new GenericTransformer<>(VisitDto::new, (visit, dto) -> {
            dto.setId(visit.getId());
            dto.setDate(visit.getDate());
            dto.setDescription(visit.getDescription());
        });
    }

    public static GenericTransformer<Pet, VisitEditDto> toEditDto() {
        return new GenericTransformer<>(VisitEditDto::new, (pet, dto) -> {
            dto.setPet(PetTransformers.toDto().apply(pet));
            Owner owner = pet.getOwner();
            dto.setOwnerName(String.format("%s %s", owner.getFirstName(), owner.getLastName()));
            dto.setVisit(new VisitDto());
        });
    }

}
