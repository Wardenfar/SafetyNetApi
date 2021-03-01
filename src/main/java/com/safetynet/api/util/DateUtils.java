package com.safetynet.api.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    // The date format used to encode dates in Strings
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

    // TEST ONLY : used to replace the "now" date
    private static LocalDate fakeCurrentDate = null;

    /**
     * Parse String to LocalDate
     */
    public static LocalDate parseDate(String str) {
        return LocalDate.parse(str, dateFormatter);
    }

    /**
     * Format LocalDate to String
     */
    public static String formatDate(LocalDate date) {
        return date.format(dateFormatter);
    }

    /**
     * Compute age from birthdate and now
     */
    public static int ageInYears(LocalDate birthdate) {
        return diffInYears(birthdate, getNow());
    }

    /**
     * Compute diff from one data to an other
     */
    public static int diffInYears(LocalDate from, LocalDate to) {
        return Period.between(from, to).getYears();
    }

    /**
     * Return the "now" date
     */
    public static LocalDate getNow() {
        if (fakeCurrentDate != null) {
            return fakeCurrentDate;
        } else {
            return LocalDate.now();
        }
    }

    /**
     * TEST ONLY : set the fake current Date
     */
    public static void setFakeCurrentDate(LocalDate date) {
        fakeCurrentDate = date;
    }
}
