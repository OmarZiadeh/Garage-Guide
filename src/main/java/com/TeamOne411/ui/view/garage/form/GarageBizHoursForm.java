package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.time.Duration;
import java.util.Locale;

public class GarageBizHoursForm extends VerticalLayout {
    @SuppressWarnings("FieldCanBeLocal")
    private final TextField dayOfTheWeek = new TextField("Day");
    private final RadioButtonGroup<String> isOpen = new RadioButtonGroup<>();
    private final TimePicker openTime = new TimePicker("Opening Time");
    private final TimePicker closeTime = new TimePicker("Closing Time");
    Binder<BusinessHours> binder = new BeanValidationBinder<>(BusinessHours.class);
    private BusinessHours businessHours = new BusinessHours();

    public GarageBizHoursForm() {

        // initial view setup
        addClassName("garage-business-hours-form");

        // centers the form contents within the window
        setAlignItems(Alignment.CENTER);

        // bind instance fields to the form fields
        binder.bindInstanceFields(this);

        // set field attributes
        dayOfTheWeek.setReadOnly(true);
        openTime.setLocale(Locale.US);
        openTime.setStep(Duration.ofMinutes(30));
        closeTime.setLocale(Locale.US);
        closeTime.setStep(Duration.ofMinutes(30));
        isOpen.setRequired(true);
        isOpen.setItems("Open", "Closed");
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        // LISTENERS
        isOpen.addValueChangeListener(e ->
        {
            if (isOpen.getValue().equals("Open")) {
                openTime.setRequired(true);
                closeTime.setRequired(true);
                openTime.setEnabled(true);
                closeTime.setEnabled(true);
                businessHours.setOpen(isOpen.getValue().equals("Open"));
            } else {
                openTime.setRequired(false);
                closeTime.setRequired(false);
                openTime.setEnabled(false);
                closeTime.setEnabled(false);
                openTime.setValue(null);
                closeTime.setValue(null);
                businessHours.setOpen(isOpen.getValue().equals("Closed"));
            }
        });
        openTime.addValueChangeListener(e -> {
            if (openTime.getValue() != null) {
                closeTime.setMinTime(openTime.getValue().plusMinutes(30));
            }
        });
        closeTime.addValueChangeListener(e -> {
            if (closeTime.getValue() != null) {
                openTime.setMaxTime(closeTime.getValue().minusMinutes(30));
            }
        });
        saveButton.addClickListener(e -> fireEvent(new GarageBizHoursForm.SaveEvent(this)));
        cancelButton.addClickListener(e -> fireEvent(new GarageBizHoursForm.CancelEvent(this)));

        // add fields to the form
        add(
                dayOfTheWeek,
                isOpen,
                openTime,
                closeTime,
                new HorizontalLayout(saveButton, cancelButton));
    }

    /**
     * Fills form with existing business hours for the provided BusinessHours instance.
     */
    public void prefillForm(BusinessHours businessHours) {
        this.businessHours = businessHours;
        binder.readBean(businessHours);
        isOpen.setValue(businessHours.getOpen() ? "Open" : "Closed");
    }

    /**
     * Attempts to return a BusinessHours instance from the values of the form controls.
     *
     * @return a BusinessHours instance
     */
    public BusinessHours getBusinessHours() {
        try {
            binder.writeBean(businessHours);
            return businessHours;
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when save button is clicked
     */
    public static class SaveEvent extends ComponentEvent<GarageBizHoursForm> {
        SaveEvent(GarageBizHoursForm source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when cancel button is clicked
     */
    public static class CancelEvent extends ComponentEvent<GarageBizHoursForm> {
        CancelEvent(GarageBizHoursForm source) {
            super(source, false);
        }
    }
}