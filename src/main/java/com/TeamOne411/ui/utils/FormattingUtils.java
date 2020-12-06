package com.TeamOne411.ui.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This class provides various methods for formatting various objects such as dates and times
 */
public class FormattingUtils {

    /**
     * Formats hours with am/pm. E.g: 2:00 PM for use in grids
     */
    public static String convertTime(LocalTime time){
        if(time != null){
            DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
            return hourFormat.format(time);
        }
        return null;
    }

    /**
     * Formats hours with am/pm. E.g: 2:00 PM for use by the LocalTimeConverter
     */
    public static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter
            .ofPattern("h:mm a", Locale.US);

}