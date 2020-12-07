package com.TeamOne411.ui.utils;

import java.time.LocalDate;

import com.vaadin.flow.templatemodel.ModelEncoder;

import static com.TeamOne411.ui.utils.FormattingUtils.FULL_DATE_FORMATTER;
import static com.TeamOne411.ui.utils.FormattingUtils.convertIfNotNull;

/**
 * This class converts a date value to/from a string value
 * Class methods provided by Vaadin sample application
 */
public class LocalDateConverter implements ModelEncoder<LocalDate, String> {

    @Override
    public String encode(LocalDate modelValue) {
        return convertIfNotNull(modelValue, FULL_DATE_FORMATTER::format);
    }

    @Override
    public LocalDate decode(String presentationValue) {
        throw new UnsupportedOperationException();
    }
}