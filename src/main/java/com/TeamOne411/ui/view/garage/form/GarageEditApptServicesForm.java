package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.entity.schedule.AppointmentTask;
import com.TeamOne411.backend.service.AppointmentService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import java.math.BigDecimal;
import java.util.List;

/**
 * This form allows a garage to edit the services that were provided for an appointment
 */
public class GarageEditApptServicesForm extends VerticalLayout {
    private final Appointment appointment;
    private final AppointmentService appointmentService;
    private final GridPro<AppointmentTask> appointmentTaskGrid = new GridPro<>();

    public GarageEditApptServicesForm(Appointment appointment, AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
        this.appointment = appointment;

        // initial view setup
        addClassName("garage-business-hours-form");

        // centers the form contents within the window
        setAlignItems(Alignment.CENTER);

        appointmentTaskGrid.setClassName("appt-task-grid");
        appointmentTaskGrid.removeAllColumns();
        appointmentTaskGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        appointmentTaskGrid.addColumn(appointmentTask -> appointmentTask.getOfferedService().getServiceName())
                .setHeader("Service").setResizable(true);
        appointmentTaskGrid.addEditColumn(AppointmentTask::getGarageComments).text(AppointmentTask::setGarageComments)
                .setHeader("Comments").setResizable(true);
        appointmentTaskGrid.addComponentColumn(this::createDeleteButton).setHeader("Delete")
                .setTextAlign(ColumnTextAlign.CENTER);

        appointmentTaskGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        // BUTTONS
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        // LISTENERS
        saveButton.addClickListener(e -> fireEvent(new SaveEvent(this)));
        cancelButton.addClickListener(e -> fireEvent(new CancelEvent(this)));
        appointmentTaskGrid.addItemPropertyChangedListener(e -> appointmentService.saveAppointmentTask(e.getItem()));


        // add fields to the form
        add(
                appointmentTaskGrid,
                new HorizontalLayout(saveButton, cancelButton));

        // populate the items in the task grid
        updateAppointmentTaskGrid();
    }

    /**
     * Updates the appointment task grid
     */
    private void updateAppointmentTaskGrid() {
        appointmentTaskGrid.setItems(appointmentService.findAllAppointmentTasksByAppointment(appointment));
    }

    public void calculateEstimatedTotalPrice() {
        BigDecimal calcTotal = new BigDecimal(0);
        List<AppointmentTask> appointmentTaskList = appointmentService.findAllAppointmentTasksByAppointment(appointment);
        for (AppointmentTask appointmentTask : appointmentTaskList) {
            calcTotal = calcTotal.add(appointmentTask.getPrice());
        }
        appointment.setEstimatedTotalPrice(calcTotal);
        appointmentService.saveAppointment(appointment);
    }

    /**
     * Creates the delete icon button for each row in the grid
     *
     * @param appointmentTask the appointment task instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button createDeleteButton(AppointmentTask appointmentTask) {
        Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create(), buttonClickEvent ->
                deleteClick(appointmentTask));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return deleteButton;
    }

    /**
     * Fired on deleteClick(). Shows a confirm dialog and then deletes the selected appointment task
     */
    private void deleteClick(AppointmentTask appointmentTask) {
        String message = "Are you sure you want to delete this Service?";
        ConfirmDialog confirmDeleteDialog = new ConfirmDialog(
                "Delete Appointment Service", message,
                "Delete",
                e -> onDeleteConfirm(appointmentTask),
                "Cancel",
                e -> e.getSource().close());

        confirmDeleteDialog.setConfirmButtonTheme("error primary");
        confirmDeleteDialog.open();
    }

    /**
     * Fired when delete confirm dialog is confirmed by user. Deletes selected AppointmentTask
     */
    private void onDeleteConfirm(AppointmentTask appointmentTask) {
        if (appointmentTask != null) {
            appointmentService.deleteAppointmentTask(appointmentTask);
            updateAppointmentTaskGrid();
            String successMessage = "The selected service has been deleted from this appointment";

            Notification notification = new Notification(
                    successMessage,
                    4000,
                    Notification.Position.TOP_END
            );
            notification.open();
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when save button is clicked
     */
    public static class SaveEvent extends ComponentEvent<GarageEditApptServicesForm> {
        SaveEvent(GarageEditApptServicesForm source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when cancel button is clicked
     */
    public static class CancelEvent extends ComponentEvent<GarageEditApptServicesForm> {
        CancelEvent(GarageEditApptServicesForm source) {
            super(source, false);
        }
    }
}
