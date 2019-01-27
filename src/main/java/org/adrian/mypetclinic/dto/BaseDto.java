package org.adrian.mypetclinic.dto;

import lombok.Getter;
import lombok.Setter;
import org.adrian.mypetclinic.domain.BaseEntity;

@Getter
@Setter
public class BaseDto {
    private Long id;

    public static BaseDto fromBaseEntity(BaseEntity entity) {
        BaseDto dto = new BaseDto();
        dto.setId(entity.getId());
        return dto;
    }
}
