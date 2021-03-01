package com.safetynet.api.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

    private static LocalDate fakeCurrentDate = null;

    public static LocalDate parseDate(String str) {
        return LocalDate.parse(str, dateFormatter);
    }

    public static String formatDate(LocalDate date) {
        return date.format(dateFormatter);
    }

    public static int ageInYears(LocalDate birthdate) {
        return diffInYears(birthdate, getNow());
    }


    public static int diffInYears(LocalDate from, LocalDate to) {
        return Period.between(from, to).getYears();
    }

    public static LocalDate getNow() {
        if (fakeCurrentDate != null) {
            return fakeCurrentDate;
        } else {
            return LocalDate.now();
        }
    }

    public static void setFakeCurrentDate(LocalDate date) {
        fakeCurrentDate = date;
    }
}
