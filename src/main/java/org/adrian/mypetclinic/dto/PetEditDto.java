package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;
import org.adrian.mypetclinic.domain.PetType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PetEditDto {

    @NotNull
    private String name;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private PetType type;
    private List<PetTypeDto> petTypes;
}
