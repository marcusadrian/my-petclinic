package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PersonDto extends BaseDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

}
