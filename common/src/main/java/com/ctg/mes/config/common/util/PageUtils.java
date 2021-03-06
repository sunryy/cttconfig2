package com.ctg.mes.config.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author henry
 */
public class PageUtils {
    public static int getFirstResult(int pageNumber, int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("[pageSize] must great than zero");
        }
        return (pageNumber - 1) * pageSize;
    }

    public static Integer[] generateLinkPageNumbers(int currentPageNumber, int lastPageNumber, int count) {
        int avg = count / 2;

        int startPageNumber = currentPageNumber - avg;
        if (startPageNumber <= 0) {
            startPageNumber = 1;
        }

        int endPageNumber = startPageNumber + count - 1;
        if (endPageNumber > lastPageNumber) {
            endPageNumber = lastPageNumber;
        }

        if (endPageNumber - startPageNumber < count) {
            startPageNumber = endPageNumber - count;
            if (startPageNumber <= 0) {
                startPageNumber = 1;
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int i = startPageNumber; i <= endPageNumber; i++) {
            result.add(i);
        }
        return result.toArray(new Integer[0]);
    }

    public static int computeLastPageNumber(int totalElements, int pageSize) {
        int result = totalElements % pageSize == 0 ? totalElements / pageSize : totalElements / pageSize + 1;

        if (result <= 1) {
            result = 1;
        }
        return result;
    }

    public static int computePageNumber(int pageNumber, int pageSize, int totalElements) {
        if (pageNumber <= 1) {
            return 1;
        }
        if ((2147483647 == pageNumber) || (pageNumber > computeLastPageNumber(totalElements, pageSize))) {
            return computeLastPageNumber(totalElements, pageSize);
        }
        return pageNumber;
    }

    public static <T> List<T> pageResult(List<T> list, int pageNumber, int pageSize) {

         int size = list.size();
         int start = pageSize * (pageNumber - 1);
         int totalPage = size/pageSize;


        return size>start?list.subList(pageSize * (pageNumber - 1),
                Math.min(pageSize * pageNumber, size)): list.subList(pageSize * totalPage,
                size);
    }
}