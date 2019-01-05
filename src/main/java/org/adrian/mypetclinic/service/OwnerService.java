package org.adrian.mypetclinic.service;

import com.querydsl.core.types.Predicate;
import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.predicate.PetPredicates;
import org.adrian.mypetclinic.repo.OwnerRepository;
import org.adrian.mypetclinic.repo.PetRepository;
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

    public OwnerService(OwnerRepository repository, PetRepository petRepository) {
        this.repository = repository;
        this.petRepository = petRepository;
    }

    @Transactional(readOnly = true)
    public <T> Page<T> findOwners(Predicate predicate, Function<Owner, T> transformer, Pageable pageable) {
        Page<Owner> ownerPage = repository.findAll(predicate, pageable);
        return GeneralTransformers.pageTransformer(transformer, pageable).apply(ownerPage);
    }

    @Transactional(readOnly = true)
    public Page<Owner> findOwners(Predicate predicate, Pageable pageable) {
        return findOwners(predicate, Function.identity(), pageable);
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findById(Long id, Function<Owner, T> transformer) {
        return repository.findById(id).map(transformer);
    }

    @Transactional(readOnly = true)
    public Optional<Owner> findById(Long id) {
        return findById(id, Function.identity());
    }

    @Transactional
    public <T> void updateOwner(Long id, T t, BiConsumer<T, Owner> biConsumer) {
        Owner owner = this.repository.findById(id).get();
        biConsumer.accept(t, owner);
    }

    @Transactional
    public <T> void updatePet(Long ownerId, Long petId, T t, BiConsumer<T, Pet> biConsumer) {
        Pet pet = this.petRepository
                .findOne(PetPredicates.findByIdPath(ownerId, petId))
                .orElseThrow(() -> new IllegalArgumentException(String.format("no pet with id=%s and owner.id=%s",
                        petId, ownerId)));
        biConsumer.accept(t, pet);
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
        return repository.findById(ownerId)
                .flatMap(owner -> owner.getPets()
                        .stream()
                        .filter(pet -> pet.getId().equals(petId))
                        .findFirst())
                .map(transformer);
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findVisit(Long ownerId, Long petId, Long visitId, Function<Visit, T> transformer) {
        return repository.findById(ownerId)
                .flatMap(owner -> owner.getPets()
                        .stream()
                        .filter(pet -> pet.getId().equals(petId))
                        .findFirst())
                .flatMap(pet -> pet.getVisits()
                        .stream()
                        .filter(visit -> visit.getId().equals(visitId))
                        .findFirst())
                .map(transformer);
    }

    @Transactional(readOnly = true)
    public Optional<Pet> findPet(Long ownerId, Long petId) {
        return this.findPet(ownerId, petId, Function.identity());
    }

    @Transactional(readOnly = true)
    public <T> T newPet(Long ownerId, Function<Pet, T> transformer) {
        Pet pet = new Pet();
        pet.setOwner(findById(ownerId).get());
        return transformer.apply(pet);
    }

    @Transactional(readOnly = true)
    public <T> T newVisit(Long ownerId, Long petId, Function<Visit, T> transformer) {
        Visit visit = new Visit();
        visit.setPet(findPet(ownerId, petId).get());
        return transformer.apply(visit);
    }

}