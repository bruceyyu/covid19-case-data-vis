package comp3111.covid.core;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class utils {
    /**
     * convert LocalDate type (used by JavaFx's datepicker) to Date type
     * @param raw The date in LocalDate type
     * @return the date in Date type
     */
    public static Date localDateToDate(LocalDate raw) {
        if (raw == null) return null;
        Instant instant = Instant.from(raw.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        return date;
    }

    public static LocalDate dateToLocalDate(Date raw) {
        if (raw == null) return null;
        return raw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    /**
     * convert LocalDate type (used by JavaFx's datepicker) to a date string
     * @param raw The date in LocalDate type
     * @param format The desired format of the date string
     * @return the formatted date string
     */
    public static String localDateToString(LocalDate raw, String format) {
        if (raw == null) return null;
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);
        String output = dtf.format(raw);
        return output;
    }
}
