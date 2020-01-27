package org.adrian.mypetclinic.predicate;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.adrian.mypetclinic.domain.QOwner;
import org.adrian.mypetclinic.service.OwnerSearchCriteria;
import org.adrian.mypetclinic.util.QuerydslPredicateUtils;

public class OwnerSearchPredicates {

    public static Predicate ownerSearch(OwnerSearchCriteria criteria) {
        return ExpressionUtils.allOf(
                firstName(criteria),
                lastName(criteria),
                address(criteria),
                city(criteria),
                telephone(criteria),
                petName(criteria));
    }

    private static BooleanExpression firstName(OwnerSearchCriteria criteria) {
        return QuerydslPredicateUtils.createPredicate(criteria,
                OwnerSearchCriteria::getFirstName,
                QOwner.owner.firstName::startsWithIgnoreCase);
    }

    private static BooleanExpression lastName(OwnerSearchCriteria criteria) {
        return QuerydslPredicateUtils.createPredicate(criteria,
                OwnerSearchCriteria::getLastName,
                QOwner.owner.lastName::startsWithIgnoreCase);
    }

    private static BooleanExpression address(OwnerSearchCriteria criteria) {
        return QuerydslPredicateUtils.createPredicate(criteria,
                OwnerSearchCriteria::getAddress,
                QOwner.owner.address::containsIgnoreCase);
    }

    private static BooleanExpression city(OwnerSearchCriteria criteria) {
        return QuerydslPredicateUtils.createPredicate(criteria,
                OwnerSearchCriteria::getCity,
                QOwner.owner.city::startsWithIgnoreCase);
    }

    private static BooleanExpression telephone(OwnerSearchCriteria criteria) {
        return QuerydslPredicateUtils.createPredicate(criteria,
                OwnerSearchCriteria::getTelephone,
                QOwner.owner.telephone::containsIgnoreCase);
    }

    private static BooleanExpression petName(OwnerSearchCriteria criteria) {
        return QuerydslPredicateUtils.createPredicate(criteria,
                OwnerSearchCriteria::getPetName,
                QOwner.owner.pets.any().name::startsWithIgnoreCase);
    }

}
