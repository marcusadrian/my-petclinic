package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.dto.VisitDto;

public class VisitTransformers {
    public static GenericTransformer<Visit, VisitDto> toDto() {
        return new GenericTransformer<>(VisitDto::new, (visit, dto) -> {
            dto.setId(visit.getId());
            dto.setDate(visit.getDate());
            dto.setDescription(visit.getDescription());
        });

    }
}
