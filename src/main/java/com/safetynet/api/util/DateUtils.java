package com.safetynet.api.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

    public static LocalDate parseDate(String str) {
        return LocalDate.parse(str, dateFormatter);
    }

    public static String formatDate(LocalDate date){
        return date.format(dateFormatter);
    }

    public static int ageInYears(LocalDate birthdate) {
        return diffInYears(birthdate, LocalDate.now());
    }

    public static int diffInYears(LocalDate from, LocalDate to) {
        return Period.between(from, to).getYears();
    }
}
