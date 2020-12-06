package com.TeamOne411.ui.view.carowner.childview;

import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.service.*;
import com.TeamOne411.ui.view.carowner.form.AppointmentDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CarOwnerAppointmentsView extends VerticalLayout {
    private final AppointmentService appointmentService;
    private AppointmentDialog appointmentDialog;
    private final Grid<Appointment> upcomingAppointments = new Grid<>(Appointment.class);

    public CarOwnerAppointmentsView(AppointmentService appointmentService,
                                    ServiceCatalogService serviceCatalogService,
                                    GarageCalendarService garageCalendarService) {
        this.appointmentService = appointmentService;

        Button newAppointment = new Button("Schedule New Appointment");
        newAppointment.addClickListener(e -> showAppointmentDialog(serviceCatalogService,
                garageCalendarService));

        upcomingAppointments.setClassName("upcoming-grid");
        upcomingAppointments.setColumns("appointmentDate", "appointmentTime");
        upcomingAppointments.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        upcomingAppointments.addComponentColumn(this::cancelButton).setHeader("Cancel")
                .setTextAlign(ColumnTextAlign.CENTER);

        // LAYOUTS
        // create the layout for the upcoming appointments
        VerticalLayout upcomingLayout = new VerticalLayout(
                new H4("Upcoming Appointments"),
                upcomingAppointments
        );
        upcomingLayout.setClassName("upcoming-appointments");
        setLayoutAttributes(upcomingLayout);

        // create the layout for the past appointments
        VerticalLayout pastLayout = new VerticalLayout(
                new H4("Past Appointments")
        );
        pastLayout.setClassName("past-appointments");
        setLayoutAttributes(pastLayout);

        HorizontalLayout combinedLayout = new HorizontalLayout(upcomingLayout, pastLayout);
        combinedLayout.setSizeFull();

        // add the upcoming and past appointments to the parent layout
        add(newAppointment, combinedLayout);

        // populate the grids
        updateUpcomingGrid();
    }

    /**
     * refreshes the upcoming appointments grid
     */
    private void updateUpcomingGrid(){
        upcomingAppointments.setItems(appointmentService.findAllAppointments());
    }

    /**
     * Sets common attributes for the layouts
     */
    private void setLayoutAttributes(VerticalLayout verticalLayout){
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.getStyle().set("border", "1px solid #9E9E9E");
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setWidth("40%");
    }

    /**
     * Creates the cancel icon button for each row in the grid
     *
     * @param appointment the Appointment instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button cancelButton(Appointment appointment) {
        Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create(), buttonClickEvent ->
                cancelClick(appointment));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return deleteButton;
    }

    /**
     * Fired on cancelClick(). Shows a confirm dialog and then cancels the selected appointment & removes from the grid
     */
    private void cancelClick(Appointment appointment) {

        String message = "Are you sure you want to cancel this appointment?";
        ConfirmDialog confirmDeleteDialog = new ConfirmDialog(
                "Cancel Appointment", message,
                "Proceed",
                e -> onCancelConfirm(appointment),
                "Exit",
                e -> e.getSource().close());

        confirmDeleteDialog.setConfirmButtonTheme("error primary");
        confirmDeleteDialog.open();
    }


    /**
     * Fired when cancel confirm dialog is confirmed by user. Cancels the appointment and clears the time slots
     */
    private void onCancelConfirm(Appointment appointment) {
        /* TODO FINISH THIS
        if (appointment != null) {

            garageCalendarService.deleteClosedDate(closedDate);
            updateDatesClosedList();
            String successMessage = "Deleted " + closedDate.getNotOpenDate() + " from Dates Closed List";

            Notification notification = new Notification(
                    successMessage,
                    4000,
                    Notification.Position.TOP_END
            );
            notification.open();
        }
        */
    }

    /**
     * Opens the appointment dialog
     */
    private void showAppointmentDialog(ServiceCatalogService serviceCatalogService,
                                       GarageCalendarService garageCalendarService) {
        appointmentDialog = new AppointmentDialog(appointmentService, serviceCatalogService,
                garageCalendarService);
        appointmentDialog.setWidth("50%");
        appointmentDialog.setHeightFull();
        appointmentDialog.addListener(AppointmentDialog.SaveSuccessEvent.class,
                this::onSave);
        appointmentDialog.open();
    }

    /**
     * Fired when the AppointmentForm has been exited.
     * @param event the event that fired this method
     */
    private void onSave(ComponentEvent<AppointmentDialog> event) {
        appointmentDialog.close();
        updateUpcomingGrid();
    }
}
