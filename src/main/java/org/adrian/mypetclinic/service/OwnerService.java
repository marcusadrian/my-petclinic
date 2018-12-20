package org.adrian.mypetclinic.service;

import com.querydsl.core.types.Predicate;
import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.QOwner;
import org.adrian.mypetclinic.repo.OwnerRepository;
import org.adrian.mypetclinic.transform.GeneralTransformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Function;

@Service
public class OwnerService {
    private final OwnerRepository repository;

    public OwnerService(OwnerRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public <T> Page<T> findByLastNameStartingWith(String lastName, Function<Owner, T> transformer, Pageable pageable) {

        Page<Owner> ownerPage;
        if (StringUtils.hasText(lastName)) {
            Predicate predicate = QOwner.owner.lastName.startsWithIgnoreCase(lastName);
            ownerPage = repository.findAll(predicate, pageable);
        } else {
            ownerPage = repository.findAll(pageable);
        }
        return GeneralTransformers.pageTransformer(transformer, pageable).apply(ownerPage);
    }

    @Transactional(readOnly = true)
    public Page<Owner> findByLastNameStartingWith(String lastName, Pageable pageable) {
        return findByLastNameStartingWith(lastName, Function.identity(), pageable);
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findById(Long id, Function<Owner, T> transformer) {
        return repository.findById(id).map(transformer);
    }

    @Transactional(readOnly = true)
    public Optional<Owner> findById(Long id) {
        return findById(id, Function.identity());
    }


}