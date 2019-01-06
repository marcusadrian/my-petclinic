package org.adrian.mypetclinic.service;

import com.querydsl.core.types.Predicate;
import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.predicate.PetPredicates;
import org.adrian.mypetclinic.predicate.VisitPredicates;
import org.adrian.mypetclinic.repo.OwnerRepository;
import org.adrian.mypetclinic.repo.PetRepository;
import org.adrian.mypetclinic.repo.VisitRepository;
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
    private final PetRepository petRepository;
    private final VisitRepository visitRepository;

    public OwnerService(OwnerRepository repository, PetRepository petRepository, VisitRepository visitRepository) {
        this.repository = repository;
        this.petRepository = petRepository;
        this.visitRepository = visitRepository;
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
    public <T> void updatePet(Long ownerId, Long petId, T t, BiConsumer<T, Pet> biConsumer) {
        Pet pet = findPet(ownerId, petId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("no pet with id=%s, owner.id=%s", petId, ownerId)));
        biConsumer.accept(t, pet);
    }

    @Transactional
    public Optional<Pet> findPet(Long ownerId, Long petId) {
        return this.petRepository.findOne(PetPredicates.findByIdPath(ownerId, petId));
    }

    @Transactional
    public <T> Long createOwner(T t, Function<T, Owner> transformer) {
        return this.repository.save(transformer.apply(t)).getId();
    }

    @Transactional
    public void deleteOwner(Long ownerId) {
        this.repository.deleteById(ownerId);
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findPet(Long ownerId, Long petId, Function<Pet, T> transformer) {
        return this.findPet(ownerId, petId).map(transformer);
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findVisit(Long ownerId, Long petId, Long visitId, Function<Visit, T> transformer) {
        return visitRepository
                .findOne(VisitPredicates.findByIdPath(ownerId, petId, visitId))
                .map(transformer);
    }

    @Transactional(readOnly = true)
    public <T> T newPet(Long ownerId, Function<Pet, T> transformer) {
        Owner owner = findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("no owner with id=%s", ownerId)));
        Pet pet = new Pet();
        pet.setOwner(owner);
        return transformer.apply(pet);
    }

    @Transactional(readOnly = true)
    public <T> T newVisit(Long ownerId, Long petId, Function<Visit, T> transformer) {
        Pet pet = findPet(ownerId, petId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("no pet with id=%s, owner.id=%s", petId, ownerId)));
        Visit visit = new Visit();
        visit.setPet(pet);
        return transformer.apply(visit);
    }

}