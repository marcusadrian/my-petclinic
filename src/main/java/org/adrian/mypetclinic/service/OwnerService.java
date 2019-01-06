package org.adrian.mypetclinic.service;

import com.querydsl.core.types.Predicate;
import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.repo.OwnerRepository;
import org.adrian.mypetclinic.transform.GeneralTransformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class OwnerService {
    private final OwnerRepository repository;

    public OwnerService(OwnerRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public <T> Page<T> findOwners(Predicate predicate, Function<Owner, T> transformer, Pageable pageable) {
        Page<Owner> ownerPage = repository.findAll(predicate, pageable);
        return GeneralTransformers.pageTransformer(transformer, pageable).apply(ownerPage);
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findById(Long id, Function<Owner, T> transformer) {
        return this.findById(id).map(transformer);
    }

    @Transactional(readOnly = true)
    public Optional<Owner> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public <T> void updateOwner(Long id, T t, BiConsumer<T, Owner> biConsumer) {
        Owner owner = this.repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("no owner with id=%s", id)));
        biConsumer.accept(t, owner);
    }

    @Transactional
    public <T> Long createOwner(T t, Function<T, Owner> transformer) {
        return this.repository.save(transformer.apply(t)).getId();
    }

    @Transactional
    public void deleteOwner(Long ownerId) {
        this.repository.deleteById(ownerId);
    }

}