package org.adrian.mypetclinic.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerSummaryDto extends BaseDto {

    private String name;
    private String address;
    private String city;
    private String telephone;
    private String petNames;

}
