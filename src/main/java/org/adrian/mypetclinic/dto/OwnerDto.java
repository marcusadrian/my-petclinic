package org.adrian.mypetclinic.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class OwnerDto extends PersonDto {

    private String address;
    private String city;
    private String telephone;

    private Set<PetDto> pets;

}
