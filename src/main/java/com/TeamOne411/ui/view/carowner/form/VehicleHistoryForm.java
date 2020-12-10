package com.TeamOne411.ui.view.carowner.form;

import com.TeamOne411.backend.entity.Vehicle;
import com.TeamOne411.backend.entity.schedule.AppointmentTask;
import com.TeamOne411.backend.service.AppointmentService;
import com.TeamOne411.ui.utils.FormattingUtils;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;


/**
 * This form shows the history for a given vehicle
 */
public class VehicleHistoryForm extends VerticalLayout {
    Grid<AppointmentTask> grid = new Grid<>(AppointmentTask.class);
    H5 noHistoryAvailable = new H5("This vehicle does not have any service history recorded.");

    public VehicleHistoryForm(Vehicle vehicle, AppointmentService appointmentService) {

        Button exitButton = new Button("Exit");
        exitButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        addClassName("service-history-form");
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        // populate the appointment tasks in the grid
        if (appointmentService.findAllAppointmentTasksByVehicle(vehicle).isEmpty()) {
            grid.setVisible(false);
            noHistoryAvailable.setVisible(true);
        } else {
            grid.setItems(appointmentService.findAllAppointmentTasksByVehicle(vehicle));
            noHistoryAvailable.setVisible(false);
        }

        // configure the categories-grid
        grid.addClassName("history-grid");
        grid.setHeightByRows(true);
        grid.removeAllColumns();

        grid.addColumn(appointmentTask -> FormattingUtils.SHORT_DATE_FORMATTER
                .format(appointmentTask.getAppointment().getAppointmentDate())).setHeader("Date").setResizable(true);
        grid.addColumn(appointmentTask -> appointmentTask.getOfferedService().getServiceName()).setHeader("Service")
                .setResizable(true);
        grid.addColumn(AppointmentTask::getGarageComments).setHeader("Garage Comments").setResizable(true);

        // sets column widths
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // LISTENERS
        exitButton.addClickListener(e -> fireEvent(new ExitEvent(this)));

        // Add grid and exit button to the layout
        add(new H4(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel() + " " + vehicle.getVin()),
                noHistoryAvailable, grid, exitButton);
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when exit button is clicked
     */
    public static class ExitEvent extends ComponentEvent<VehicleHistoryForm> {
        ExitEvent(VehicleHistoryForm source) {
            super(source, false);
        }
    }
}
