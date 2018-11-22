package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Identifiable;

@Getter
@Setter
public class BaseDto implements Identifiable<Long> {
    private Long id;
}
