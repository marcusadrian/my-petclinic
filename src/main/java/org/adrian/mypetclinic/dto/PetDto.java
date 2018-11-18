package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PetDto extends NamedDto {

    private LocalDate birthDate;
    private String type;
    private List<VisitDto> visits;

}
