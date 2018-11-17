package org.adrian.mypetclinic.repo;

import org.adrian.mypetclinic.domain.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository class for {@link PetType} domain objects.
 *
 * @author Marcus Adrian
 */
public interface PetTypeRepository extends JpaRepository<PetType, Long> {
    Optional<PetType> findByName(String name);
}
