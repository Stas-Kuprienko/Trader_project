package com.anastasia.core_service.utility;

import com.anastasia.core_service.exception.PaginationException;
import com.anastasia.trade_project.enums.Sorting;
import com.anastasia.trade_project.markets.Securities;
import java.util.Comparator;
import java.util.List;

public final class PaginationUtility {

    private PaginationUtility() {}


    public static List<? extends Securities> sorting(List<? extends Securities> toSort, Sorting sortingCase, Sorting.Direction direction) {
        return switch (sortingCase) {
            case Sorting.VOLUME -> sortByTradeVolume(toSort, direction);
            case Sorting.ALPHABET -> sortByAlphabet(toSort, direction);
            default -> throw PaginationException.unsupportedSorting(sortingCase);
        };
    }

    public static List<? extends Securities> findPage(List<? extends Securities> toPaging, int page, int count) {
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
                return toPaging.subList(from, count);
            }
        }
    }

    public static List<? extends Securities> sortByTradeVolume(List<? extends Securities> toSort, Sorting.Direction direction) {
        switch (direction) {
            case ASC -> toSort.sort(Comparator.comparingLong(Securities::getDayTradeVolume));
            case DESC -> toSort.sort(Comparator.comparingLong(Securities::getDayTradeVolume).reversed());
        }
        return toSort;
    }

    public static List<? extends Securities> sortByAlphabet(List<? extends Securities> toSort, Sorting.Direction direction) {
        switch (direction) {
            case ASC ->toSort.sort(Comparator.comparing(Securities::getTicker));
            case DESC -> toSort.sort(Comparator.comparing(Securities::getTicker).reversed());
        }
        return toSort;
    }
}
