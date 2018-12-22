package org.adrian.mypetclinic.util;

import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.function.Function;


@Slf4j
public class QuerydslPredicateUtils {

    public static <C, P> Predicate createPredicate(
            C criteria,
            Function<C, P> propertyToTest,
            Function<P, Predicate> predicateFunction) {

        P property = propertyToTest.apply(criteria);
        if (isPropertySet(property)) {
            return predicateFunction.apply(property);
        } else {
            return null;
        }
    }

    static boolean isPropertySet(Object propertyToTest) {
        if (propertyToTest instanceof String) {
            return StringUtils.hasText((String) propertyToTest);
        } else {
            return propertyToTest != null;
        }
    }

}