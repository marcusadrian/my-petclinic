package org.adrian.mypetclinic.transform;

import org.adrian.mypetclinic.domain.Visit;
import org.adrian.mypetclinic.dto.VisitDto;
import org.adrian.mypetclinic.dto.VisitEditDto;

import java.util.function.BiConsumer;

public class VisitTransformers {
    public static GenericTransformer<Visit, VisitDto> toDto() {
        return new GenericTransformer<>(VisitDto::new, (visit, dto) -> {
            dto.setId(visit.getId());
            dto.setDate(visit.getDate());
            dto.setDescription(visit.getDescription());
        });
    }

    public static GenericTransformer<Visit, VisitEditDto> toEditDto() {
        return new GenericTransformer<>(VisitEditDto::new, (entity, dto) -> {
            dto.setDate(entity.getDate());
            dto.setDescription(entity.getDescription());
        });
    }

    public static GenericTransformer<VisitEditDto, Visit> toEntity() {
        return new GenericTransformer<>(Visit::new, (dto, entity) -> {
            entity.setDate(dto.getDate());
            entity.setDescription(dto.getDescription());
        });
    }

    public static BiConsumer<VisitEditDto, Visit> updateVisit() {
        return (dto, entity) -> {
            entity.setDate(dto.getDate());
            entity.setDescription(dto.getDescription());
        };
    }

}
