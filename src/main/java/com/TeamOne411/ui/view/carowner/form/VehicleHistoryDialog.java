package com.TeamOne411.ui.view.carowner.form;

import com.TeamOne411.backend.entity.Vehicle;
import com.TeamOne411.backend.service.AppointmentService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

/**
 * This is a dialog class that launches the VehicleHistoryForm for viewing vehicle history
 */
@SuppressWarnings("rawtypes")
public class VehicleHistoryDialog extends Dialog {

    public VehicleHistoryDialog(Vehicle vehicle, AppointmentService appointmentService) {
        VehicleHistoryForm vehicleHistoryForm = new VehicleHistoryForm(vehicle, appointmentService);
        vehicleHistoryForm.addListener(VehicleHistoryForm.ExitEvent.class, this::onExitClick);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        setResizable(true);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);

        container.add(new H3("Vehicle Service History"), vehicleHistoryForm);
        add(container);
    }

    /**
     * Fired when cancel button in child form is clicked. Closes the dialog.
     *
     * @param event event that occurred
     */
    private void onExitClick(ComponentEvent event) {
        close();
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
