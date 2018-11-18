package org.adrian.mypetclinic.service;

import org.adrian.mypetclinic.domain.Owner;
import org.adrian.mypetclinic.repo.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private final OwnerRepository repository;

    public OwnerService(OwnerRepository repository) {
        this.repository = repository;
    }


    @Transactional(readOnly = true)
    public <T> List<T> findByLastNameStartingWith(String lastName, Function<Owner, T> transformer) {
        List<Owner> owners;
        if (StringUtils.hasText(lastName)) {
            owners = repository.findByLastNameStartingWith(lastName);
        } else {
            owners = repository.findAll();
        }
        return owners.stream()
                .map(transformer)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Owner> findByLastNameStartingWith(String lastName) {
        return findByLastNameStartingWith(lastName, Function.identity());
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