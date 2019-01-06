package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.predicate.PetPredicates;
import org.adrian.mypetclinic.repo.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
        return this.repository.findOne(PetPredicates.findByIdPath(ownerId, petId));
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findPet(Long ownerId, Long petId, Function<Pet, T> transformer) {
        return this.findPet(ownerId, petId).map(transformer);
    }

    @Transactional
    public <T> void updatePet(Long ownerId, Long petId, T t, BiConsumer<T, Pet> biConsumer) {
        Pet pet = findPet(ownerId, petId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("no pet with id=%s, owner.id=%s", petId, ownerId)));
        biConsumer.accept(t, pet);
    }

    @Transactional(readOnly = true)
    public <T> T newPet(Long ownerId, Function<Pet, T> transformer) {
        Owner owner = ownerService.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("no owner with id=%s", ownerId)));
        Pet pet = new Pet();
        pet.setOwner(owner);
        return transformer.apply(pet);
    }

}
