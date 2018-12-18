package org.adrian.mypetclinic.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestBuilder {

    private int pageNumber;
    private int pageSize;
    private Sort sort;

    private PageRequestBuilder() {
    }

    public static PageRequestBuilder of(Pageable pageable) {
        PageRequestBuilder builder = new PageRequestBuilder();
        builder.pageNumber = pageable.getPageNumber();
        builder.pageSize = pageable.getPageSize();
        builder.sort = pageable.getSort();
        return builder;
    }

    public PageRequestBuilder pageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public PageRequestBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageRequestBuilder sort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public PageRequest build() {
        return PageRequest.of(this.pageNumber, this.pageSize, this.sort);
    }
}
