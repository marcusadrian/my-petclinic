/*
 * Copyright 2002-2018 the original author or authors.
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
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.adrian.mypetclinic.util.MyPetclinicApplicationConstants.DATE_FORMAT;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Marcus Adrian
 */
@Entity
public class Pet extends NamedEntity {

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private LocalDate birthDate;

    @Getter
    @Setter
    @ManyToOne
    private PetType type;

    @Getter
    @Setter
    @ManyToOne
    private Owner owner;

    @OneToMany(mappedBy = "pet")
    private Set<Visit> visits = new HashSet<>();

    public List<Visit> getVisits() {

        List<Visit> sortedVisits = this.visits.stream()
                .sorted(Comparator.comparing(Visit::getDate).reversed())
                .collect(Collectors.toList());

        return Collections.unmodifiableList(sortedVisits);
    }

    public void addVisit(Visit visit) {
        this.visits.add(visit);
        visit.setPet(this);
    }

}
