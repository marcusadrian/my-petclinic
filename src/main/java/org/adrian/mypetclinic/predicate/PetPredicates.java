package org.adrian.mypetclinic.predicate;

import com.querydsl.core.types.Predicate;
import org.adrian.mypetclinic.domain.QPet;

public class PetPredicates {

    public static Predicate findByIdPath(Long ownerId, Long petId) {
        return QPet.pet.owner.id.eq(ownerId)
                .and(QPet.pet.id.eq(petId));
    }
}
