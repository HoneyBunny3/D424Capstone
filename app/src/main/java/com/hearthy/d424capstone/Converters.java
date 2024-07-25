package com.hearthy.d424capstone;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Set<Integer> fromString(String value) {
        Set<Integer> set = new HashSet<>();
        if (value != null && !value.isEmpty()) {
            String[] parts = value.split(",");
            for (String part : parts) {
                set.add(Integer.parseInt(part));
            }
        }
        return set;
    }

    @TypeConverter
    public static String fromSet(Set<Integer> set) {
        if (set == null || set.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Integer value : set) {
            builder.append(value).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}