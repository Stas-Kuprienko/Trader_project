package com.project.core.utility;

import com.project.exception.PaginationException;
import com.project.enums.Sorting;
import com.project.market.Securities;
import java.util.Comparator;
import java.util.List;

public final class PaginationUtility {

    private PaginationUtility() {}


    public static <S extends Securities> List<S> sorting(List<S> toSort, Sorting sortingCase, Sorting.Direction direction) {
        return switch (sortingCase) {
            case Sorting.VOLUME -> sortByTradeVolume(toSort, direction);
            case Sorting.ALPHABET -> sortByAlphabet(toSort, direction);
            default -> throw PaginationException.unsupportedSorting(sortingCase);
        };
    }

    public static <S extends Securities> List<S> findPage(List<S> toPaging, int page, int count) {
        if (!(page > 0) || !(count > 0)) {
            throw PaginationException.illegalArguments(page, count);
        }
        if (count >= toPaging.size()) {
            if (page != 1) {
                throw PaginationException.outOfListRange(toPaging, page, count);
            }
            return toPaging;
        }
        int numberOfPages = toPaging.size() / count;
        if (page > numberOfPages) {
            throw PaginationException.outOfListRange(toPaging, page, count);
        } else {
            int from = (page - 1) * count;
            int to = from + count;
            if (to > toPaging.size()) {
                return toPaging.subList(from, toPaging.size());
            } else {
                return toPaging.subList(from, to);
            }
        }
    }

    public static <S extends Securities> List<S> sortByTradeVolume(List<S> toSort, Sorting.Direction direction) {
        switch (direction) {
            case ASC -> toSort.sort(Comparator.comparingLong(Securities::getDayTradeVolume));
            case DESC -> toSort.sort(Comparator.comparingLong(Securities::getDayTradeVolume).reversed());
        }
        return toSort;
    }

    public static <S extends Securities> List<S> sortByAlphabet(List<S> toSort, Sorting.Direction direction) {
        switch (direction) {
            case ASC -> toSort.sort(Comparator.comparing(Securities::getTicker));
            case DESC -> toSort.sort(Comparator.comparing(Securities::getTicker).reversed());
        }
        return toSort;
    }
}
