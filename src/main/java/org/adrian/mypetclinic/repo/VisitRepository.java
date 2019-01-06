package org.adrian.mypetclinic.repo;

import org.adrian.mypetclinic.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Repository class for {@link Visit} domain objects.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Marcus Adrian
 */
public interface VisitRepository extends JpaRepository<Visit, Long>, QuerydslPredicateExecutor<Visit> {
}
