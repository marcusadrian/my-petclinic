package org.adrian.mypetclinic.predicate;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.adrian.mypetclinic.domain.QPet;

public class PetPredicates {

    public static BooleanExpression owner(Long ownerId) {
        return QPet.pet.owner.id.eq(ownerId);
    }


    public static BooleanExpression id(Long petId) {
        return QPet.pet.id.eq(petId);
    }

    public static BooleanExpression name(String name) {
        return QPet.pet.name.eq(name);
    }

    public static Predicate findByIdPath(Long ownerId, Long petId) {
        return owner(ownerId).and(id(petId));
    }

}
