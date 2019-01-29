package org.adrian.mypetclinic.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.exception.PetClinicBusinessException;
import org.adrian.mypetclinic.repo.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.adrian.mypetclinic.predicate.PetPredicates.*;

@Service
public class PetService {

    private final PetRepository repository;
    private final OwnerService ownerService;

    public PetService(PetRepository repository, OwnerService ownerService) {
        this.repository = repository;
        this.ownerService = ownerService;
    }

    @Transactional
    public Optional<Pet> findPet(Long ownerId, Long petId) {
        return this.repository.findOne(findByIdPath(ownerId, petId));
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findPet(Long ownerId, Long petId, Function<Pet, T> transformer) {
        return this.findPet(ownerId, petId).map(transformer);
    }

    @Transactional
    public <T> void createPet(Long ownerId, T t, Function<T, Pet> transformer) {
        Pet pet = transformer.apply(t);
        // validate input
        validatePet(pet, ownerId, null);
        Owner owner = ownerService.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("no owner with id=%s", ownerId)));
        pet.setOwner(owner);
        this.repository.save(pet);
    }

    @Transactional
    public <T> void updatePet(Long ownerId, Long petId, T t, BiConsumer<T, Pet> biConsumer) {
        // validate input
        validatePet(t, biConsumer, ownerId, petId);
        Pet pet = findPet(ownerId, petId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("no pet with id=%s, owner.id=%s", petId, ownerId)));
        biConsumer.accept(t, pet);
    }

    private <T> void validatePet(T t, BiConsumer<T, Pet> biConsumer, Long ownerId, Long petId) {
        Pet testPet = new Pet();
        biConsumer.accept(t, testPet);
        validatePet(testPet, ownerId, petId);
    }

    private void validatePet(Pet pet, Long ownerId, Long petId) {
        // name already exists ?
        BooleanExpression predicate = owner(ownerId).and(name(pet.getName()));
        if (petId != null) { // update case
            predicate = predicate.and(id(petId).not());
        }
        if (this.repository.count(predicate) > 0) {
            throw new PetClinicBusinessException.Builder(String.format("Name '%s' already exists", pet.getName()))
                    .invalidValue(pet.getName())
                    .propertyPath("name")
                    .messageTemplate("duplicate")
                    .build();
        }
    }

    @Transactional(readOnly = true)
    public <T> T newPet(Long ownerId, Function<Pet, T> transformer) {
        Owner owner = ownerService.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("no owner with id=%s", ownerId)));
        Pet pet = new Pet();
        pet.setOwner(owner);
        return transformer.apply(pet);
    }

    @Transactional
    public void deletePet(Long ownerId, Long petId) {
        Pet pet = findPet(ownerId, petId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("no pet with id=%s, owner.id=%s", petId, ownerId)));
        this.repository.delete(pet);
    }

}
