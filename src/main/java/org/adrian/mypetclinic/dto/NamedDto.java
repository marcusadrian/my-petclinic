package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NamedDto extends BaseDto {
    @NotNull
    private String name;
}
