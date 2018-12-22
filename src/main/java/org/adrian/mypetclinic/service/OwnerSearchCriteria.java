package org.adrian.mypetclinic.service;

import lombok.Data;

@Data
public class OwnerSearchCriteria {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
}
