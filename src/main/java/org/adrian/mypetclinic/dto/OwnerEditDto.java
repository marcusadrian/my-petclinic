package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class OwnerEditDto extends PersonDto {

    @NotBlank
    private String address;
    @NotBlank
    private String city;
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    private String telephone;

}
