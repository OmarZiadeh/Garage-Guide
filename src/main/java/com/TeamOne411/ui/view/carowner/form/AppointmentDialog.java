package com.TeamOne411.ui.view.carowner.form;


import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.service.AppointmentService;
import com.TeamOne411.backend.service.GarageCalendarService;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class AppointmentDialog extends Dialog {
    private AppointmentService appointmentService;
    private AppointmentForm appointmentForm;
    private Appointment appointment;

    public AppointmentDialog(AppointmentService appointmentService, GarageService garageService,
                             ServiceCatalogService serviceCatalogService, GarageCalendarService garageCalendarService) {
        this.appointmentService = appointmentService;

        appointmentForm = new AppointmentForm(appointmentService, garageService, serviceCatalogService,
                garageCalendarService);
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
        // get the appointment and save it to the db
        appointment = appointmentForm.getAppointment();
        appointmentService.saveAppointment(appointment);

        fireEvent(new AppointmentDialog.SaveSuccessEvent(this));
    }

    /**
     * Event to emit when the appointment has been saved
     */
    public static class SaveSuccessEvent extends ComponentEvent<AppointmentDialog> {
        SaveSuccessEvent(AppointmentDialog source) {
            super(source, false);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
