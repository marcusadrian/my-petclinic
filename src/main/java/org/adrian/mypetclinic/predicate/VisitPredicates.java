package org.adrian.mypetclinic.predicate;

import com.querydsl.core.types.Predicate;
import org.adrian.mypetclinic.domain.QVisit;

public class VisitPredicates {

    public static Predicate findByIdPath(Long ownerId, Long petId, Long visitId) {
        return QVisit.visit.pet.owner.id.eq(ownerId)
                .and(QVisit.visit.pet.id.eq(petId))
                .and(QVisit.visit.id.eq(visitId));
    }

}
