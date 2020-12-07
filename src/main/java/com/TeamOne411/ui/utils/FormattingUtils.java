package com.TeamOne411.ui.utils;

import com.vaadin.flow.component.ItemLabelGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class provides various methods for formatting various objects such as dates and times
 */
public class FormattingUtils {

    /**
     * Formats hours with am/pm. E.g: 2:00 PM for use by the LocalTimeConverter
     */
    public static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter
            .ofPattern("h:mm a", Locale.US);
    /**
     * Formats dates as Monday, Mar 3, 2020. For use by the LocalDateConverter
     */
    public static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter
            .ofPattern("EEEE, LLL dd, yyyy", Locale.US);

    /**
     * Formats dates as Mar 3, 2020.
     */
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter
            .ofPattern("LLL dd, yyyy");

    /**
     * Formats hours with am/pm. E.g: 2:00 PM for use fields where the value could be null
     */
    public static String convertTime(LocalTime time) {
        if (time != null) {
            DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
            return hourFormat.format(time);
        }
        return null;
    }

    /**
     * Formats dates as Monday, Mar 3, 2020. For use in fields where the value could be null
     */
    public static String convertDate(LocalDate date) {
        if (date != null) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE, LLL dd, yyyy", Locale.US);
            return dateFormat.format(date);
        }
        return null;
    }

    /**
     * Conversion helper methods
     */
    public static <S, T> T convertIfNotNull(S source, Function<S, T> converter) {
        return convertIfNotNull(source, converter, () -> null);
    }

    public static <S, T> T convertIfNotNull(S source, Function<S, T> converter, Supplier<T> nullValueSupplier) {
        return source != null ? converter.apply(source) : nullValueSupplier.get();
    }

    public static <T> ItemLabelGenerator<T> createItemLabelGenerator(Function<T, String> converter) {
        return item -> convertIfNotNull(item, converter, () -> "");
    }

}