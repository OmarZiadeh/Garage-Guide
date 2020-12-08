package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.service.AppointmentService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;


@SuppressWarnings("rawtypes")
public class GarageAppointmentDialog extends Dialog {
    private final GarageEditApptServicesForm garageEditApptServicesForm;

    public GarageAppointmentDialog(AppointmentService appointmentService, Appointment appointment) {

        garageEditApptServicesForm = new GarageEditApptServicesForm(appointment, appointmentService);
        garageEditApptServicesForm.addListener(GarageEditApptServicesForm.CancelEvent.class, this::onCancelClick);
        garageEditApptServicesForm.addListener(GarageEditApptServicesForm.SaveEvent.class, this::onSaveClick);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        setResizable(true);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);

        container.add(new H3("Edit Appointment Services"), garageEditApptServicesForm);
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
     * Fired when save button in child form is clicked. Propagates another event depending on Add or Edit mode.
     *
     * @param event event that occurred
     */
    private void onSaveClick(ComponentEvent event) {
        garageEditApptServicesForm.calculateEstimatedTotalPrice();

        fireEvent(new GarageAppointmentDialog.SaveSuccessEvent(this));
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when appointment services have been updated.
     */
    public static class SaveSuccessEvent extends ComponentEvent<GarageAppointmentDialog> {
        SaveSuccessEvent(GarageAppointmentDialog source) {
            super(source, false);
        }
    }
}

