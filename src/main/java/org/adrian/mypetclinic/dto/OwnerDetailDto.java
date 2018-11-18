package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;
import org.adrian.mypetclinic.domain.PetType;

import java.util.List;

@Getter
@Setter
public class OwnerDetailDto extends PersonDto {

    private String address;
    private String city;
    private String telephone;

    private List<PetDto> pets;
    private List<PetType> petTypes;

}
