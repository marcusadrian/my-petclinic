package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitEditDto {
    private PetDto pet;
    private String ownerName;
    private VisitDto visit;
}
