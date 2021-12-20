package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.Locale;

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
    public static Units fromUnitString(String value) {
        return value == null ? null : Units.valueOf(value.toUpperCase(Locale.ROOT));
    }

    @TypeConverter
    public static String unitsToString(Units units)
    {
        return units.toString();
    }
}
