package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VisitDto extends BaseDto {

    private LocalDate date;
    private String description;

}
