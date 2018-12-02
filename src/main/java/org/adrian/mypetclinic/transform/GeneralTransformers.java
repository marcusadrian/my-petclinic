package org.adrian.mypetclinic.transform;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneralTransformers {

    public static <S, T> Function<Page<S>, Page<T>> pageTransformer(Function<S, T> transformer, Pageable pageable) {
        return page -> new PageImpl<>(
                page.getContent()
                        .stream()
                        .map(transformer)
                        .collect(Collectors.toList()),
                pageable,
                page.getTotalElements());
    }

}
