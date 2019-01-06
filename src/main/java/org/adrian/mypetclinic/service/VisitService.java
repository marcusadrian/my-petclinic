package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.predicate.VisitPredicates;
import org.adrian.mypetclinic.repo.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class VisitService {

    private final VisitRepository repository;
    private final PetService petService;

    public VisitService(VisitRepository repository, PetService petService) {
        this.repository = repository;
        this.petService = petService;
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findVisit(Long ownerId, Long petId, Long visitId, Function<Visit, T> transformer) {
        return this.findVisit(ownerId, petId, visitId)
                .map(transformer);
    }

    @Transactional(readOnly = true)
    public Optional<Visit> findVisit(Long ownerId, Long petId, Long visitId) {
        return repository
                .findOne(VisitPredicates.findByIdPath(ownerId, petId, visitId));
    }

    @Transactional(readOnly = true)
    public <T> T newVisit(Long ownerId, Long petId, Function<Visit, T> transformer) {
        Pet pet = petService.findPet(ownerId, petId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("no pet with id=%s, owner.id=%s", petId, ownerId)));
        Visit visit = new Visit();
        visit.setPet(pet);
        return transformer.apply(visit);
    }

    @Transactional
    public <T> void updateVisit(Long ownerId, Long petId, Long visitId, T t, BiConsumer<T, Visit> biConsumer) {
        Visit visit = findVisit(ownerId, petId, visitId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("no visit with id=%s, pet.id=%s, pet.owner.id=%s", visitId, petId, ownerId)));
        biConsumer.accept(t, visit);
    }

    @Transactional
    public void deleteVisit(Long ownerId, Long petId, Long visitId) {
        Visit visit = findVisit(ownerId, petId, visitId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("no visit with id=%s, pet.id=%s, pet.owner.id=%s", visitId, petId, ownerId)));
        this.repository.delete(visit);
    }


}
