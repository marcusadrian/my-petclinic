/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.adrian.mypetclinic.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple JavaBean domain object representing an rest.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Marcus Adrian
 */
@Entity
public class Owner extends Person {
    @Getter
    @Setter
    @NotEmpty
    private String address;

    @Getter
    @Setter
    @NotEmpty
    private String city;

    @Getter
    @Setter
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    private String telephone;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    private Set<Pet> pets = new HashSet<>();


    public void addPet(Pet pet) {
        if (pet.isNew()) {
            this.pets.add(pet);
        }
        pet.setOwner(this);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return true if pet name is already in use
     */
    public Pet getPet(String name) {
        return this.pets.stream()
                .filter(pet -> !pet.isNew())
                .filter(pet -> pet.getName().toLowerCase().equals(name.toLowerCase()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)

                .append("id", this.getId()).append("new", this.isNew())
                .append("lastName", this.getLastName())
                .append("firstName", this.getFirstName()).append("address", this.address)
                .append("city", this.city).append("telephone", this.telephone).toString();
    }
}
