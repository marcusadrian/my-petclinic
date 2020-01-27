package org.adrian.mypetclinic.predicate;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.adrian.mypetclinic.domain.QVisit;

public class VisitPredicates {

    public static BooleanExpression findByIdPath(Long ownerId, Long petId, Long visitId) {
        return QVisit.visit.pet.owner.id.eq(ownerId)
                .and(QVisit.visit.pet.id.eq(petId))
                .and(QVisit.visit.id.eq(visitId));
    }

}
