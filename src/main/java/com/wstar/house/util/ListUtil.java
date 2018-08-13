package com.wstar.house.util;

import java.util.List;

public class ListUtil {
    public static <T> String joinBySeparator(List<T> list, String separator) {
        StringBuilder result = new StringBuilder();

        if (list != null) {
            for (T o : list) {
                result.append(",").append(o.toString());
            }
            if (result.length() > 0) {
                result.deleteCharAt(0);
            }
        }

        return result.toString();
    }
}
