package com.TeamOne411.ui.view.carowner.form;


import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.service.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

/**
 * This dialog controls the opening and closing of the appointment form
 */
@SuppressWarnings("rawtypes")
public class AppointmentDialog extends Dialog {
    private final AppointmentForm appointmentForm;

    public AppointmentDialog(AppointmentService appointmentService, ServiceCatalogService serviceCatalogService,
                             GarageCalendarService garageCalendarService, VehicleService vehicleService, CarOwner carOwner) {

        appointmentForm = new AppointmentForm(serviceCatalogService, garageCalendarService, appointmentService, vehicleService, carOwner);
        appointmentForm.addListener(AppointmentForm.CancelEvent.class, this::onCancelClick);
        appointmentForm.addListener(AppointmentForm.SaveEvent.class, this::onSaveClick);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        setResizable(true);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);

        container.add(new H3("Schedule New Appointment"), appointmentForm);
        add(container);
    }

    /**
     * Fired when cancel button in child form is clicked. Closes the dialog.
     *
     * @param event event that occurred
     */
    private void onCancelClick(ComponentEvent event) {
        close();
    }

    /**
     * Fired when save button in child form is clicked. Propagates another event.
     *
     * @param event event that occurred
     */
    private void onSaveClick(ComponentEvent event) {
        //calls the appointment form to save the appointment
        appointmentForm.completeAppointment();

        fireEvent(new AppointmentDialog.SaveSuccessEvent(this));
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when the appointment has been saved
     */
    public static class SaveSuccessEvent extends ComponentEvent<AppointmentDialog> {
        SaveSuccessEvent(AppointmentDialog source) {
            super(source, false);
        }
    }
}
