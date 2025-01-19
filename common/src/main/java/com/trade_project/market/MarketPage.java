package com.trade_project.market;

import com.trade_project.enums.Sorting;
import lombok.Getter;

@Getter
public class MarketPage {

    private final int page;
    private final int count;
    private final Sorting sort;
    private final Sorting.Direction direction;


    public MarketPage(int page, int count, String sort, String direction) {

        this.page = page;
        this.count = count;
        this.sort = setSort(sort);
        this.direction = setDirection(this.sort, direction);
    }


    private Sorting setSort(String sorting) {
        if (sorting == null || sorting.isEmpty()) {
            return Sorting.VOLUME;
        } else {
            return Sorting.valueOf(sorting);
        }
    }

    private Sorting.Direction setDirection(Sorting sorting, String direction) {
        if (direction == null || direction.isEmpty()) {
            return sorting.defaultDirection;
        } else {
            return Sorting.Direction.valueOf(direction);
        }
    }


    @Override
    public String toString() {
        return "MarketPage{" +
                "page=" + page +
                ", count=" + count +
                ", sorting=" + sort +
                ", direction=" + direction +
                '}';
    }
}
