package org.adrian.mypetclinic.predicate;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.adrian.mypetclinic.domain.QOwner;
import org.adrian.mypetclinic.service.OwnerSearchCriteria;
import org.adrian.mypetclinic.util.QuerydslPredicateUtils;

public class OwnerSearchPredicates {

    public static Predicate ownerSearch(OwnerSearchCriteria criteria) {
        return ExpressionUtils.allOf(
                firstName(criteria),
                lastName(criteria));
    }

    private static Predicate firstName(OwnerSearchCriteria criteria) {
        return QuerydslPredicateUtils.createPredicate(criteria,
                OwnerSearchCriteria::getFirstName,
                QOwner.owner.firstName::startsWithIgnoreCase);
    }

    private static Predicate lastName(OwnerSearchCriteria criteria) {
        return QuerydslPredicateUtils.createPredicate(criteria,
                OwnerSearchCriteria::getLastName,
                QOwner.owner.lastName::startsWithIgnoreCase);
    }

}
