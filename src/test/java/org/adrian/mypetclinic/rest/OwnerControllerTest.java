package org.adrian.mypetclinic.rest;

import org.adrian.mypetclinic.dto.OwnerDetailDto;
import org.adrian.mypetclinic.dto.PetDto;
import org.adrian.mypetclinic.dto.VisitDto;
import org.adrian.mypetclinic.service.OwnerService;
import org.adrian.mypetclinic.transform.OwnerTransformers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OwnerControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private OwnerService service;

    @Test
    void find() throws Exception {

        // Given
        Long id = 3L;
        // {"id":3,"firstName":"Eduardo","lastName":"Rodriquez","address":"2693 Commerce St.","city":"McFarland","telephone":"6085558763","pets":[{"id":4,"name":"Jewel","birthDate":"2010-03-07","type":"dog","visits":[]},{"id":3,"name":"Rosy","birthDate":"2011-04-17","type":"dog","visits":[{"id":5,"date":"2019-11-17","description":"spayed"}]}]}
        OwnerDetailDto owner = this.service.findById(id, OwnerTransformers.toDetailDto()).get();

        // When
        ResultActions resultActions = this.mvc.perform(get("/owners/{id}", id));
        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(owner.getId()))
                .andExpect(jsonPath("$.firstName").value(owner.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(owner.getLastName()))
                .andExpect(jsonPath("$.address").value(owner.getAddress()))
                .andExpect(jsonPath("$.city").value(owner.getCity()))
                .andExpect(jsonPath("$.telephone").value(owner.getTelephone()))
                .andExpect(jsonPath("$.pets").isNotEmpty());

        int petIndex = 0;
        boolean visitTested = false;
        for (PetDto pet : owner.getPets()) {
            resultActions
                    .andExpect(jsonPath("$.pets[" + petIndex + "].id").value(pet.getId()))
                    .andExpect(jsonPath("$.pets[" + petIndex + "].name").value(pet.getName()))
                    .andExpect(jsonPath("$.pets[" + petIndex + "].type").value(pet.getType()))
                    .andExpect(jsonPath("$.pets[" + petIndex + "].birthDate").value(String.valueOf(pet.getBirthDate())));

            int visitIndex = 0;
            for (VisitDto visit : pet.getVisits()) {
                resultActions
                        .andExpect(jsonPath("$.pets[" + petIndex + "].visits[" + visitIndex + "].id").value(visit.getId()))
                        .andExpect(jsonPath("$.pets[" + petIndex + "].visits[" + visitIndex + "].date").value(String.valueOf(visit.getDate())))
                        .andExpect(jsonPath("$.pets[" + petIndex + "].visits[" + visitIndex + "].description").value(visit.getDescription()));
                visitTested = true;
                visitIndex++;
            }
            petIndex++;
        }
        // make sure you tested all paths
        assertThat(petIndex).isGreaterThan(0);
        assertThat(visitTested).isTrue();
    }

    @Test
    void prepareEdit() {
    }

    @Test
    void search() {
    }

    @Test
    void update() {
    }

    @Test
    void create() {
    }

    @Test
    void delete() {
    }
}
