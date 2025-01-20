package com.project.core_client.util;

import com.project.enums.Sorting;

import java.util.List;

public class PaginationException extends RuntimeException {

    PaginationException(String message) {
        super(message);
    }

    public static PaginationException outOfListRange(List<?> toPaging, int page, int count) {
        return new PaginationException("Out of list range. Size = %d, page = %d, count = %d."
                .formatted(toPaging.size(), page, count));
    }

    public static PaginationException illegalArguments(int page, int count) {
        return new PaginationException("Arguments must be more than 0. Page = %s, count = %d". formatted(page, count));
    }

    public static PaginationException unsupportedSorting(Sorting sorting) {
        return new PaginationException("Unsupported sorting case: " + sorting);
    }
}
