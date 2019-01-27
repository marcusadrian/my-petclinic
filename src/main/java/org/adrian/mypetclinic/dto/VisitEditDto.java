package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VisitEditDto {
    private LocalDate date;
    private String description;

}
