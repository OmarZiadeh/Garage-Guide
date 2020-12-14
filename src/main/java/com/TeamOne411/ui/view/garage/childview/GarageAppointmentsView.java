package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.Vehicle;
import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.service.AppointmentService;
import com.TeamOne411.ui.utils.FormattingUtils;
import com.TeamOne411.ui.view.carowner.form.VehicleHistoryDialog;
import com.TeamOne411.ui.view.garage.form.GarageEditApptServicesDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.Duration;
import java.util.Locale;

/**
 * This view is a vertical layout which shows the appointments for a garage
 */
public class GarageAppointmentsView extends VerticalLayout {
    private final AppointmentService appointmentService;
    private final Garage garage;
    private final GridPro<Appointment> todayGrid = new GridPro<>(Appointment.class);
    private final GridPro<Appointment> upcomingGrid = new GridPro<>(Appointment.class);
    private final H5 noAppointmentsToday = new H5("Your garage does not have any appointments scheduled for today");
    private final H5 noAppointmentsUpcoming = new H5("Your garage does not have any upcoming appointments scheduled");
    private GarageEditApptServicesDialog garageEditApptServicesDialog;

    public GarageAppointmentsView(AppointmentService appointmentService, Garage garage) {
        this.appointmentService = appointmentService;
        this.garage = garage;

        // H5 message setup
        noAppointmentsToday.setVisible(false);
        noAppointmentsUpcoming.setVisible(false);

        // GRID SETUP
        setGridAttributes(todayGrid, "today-grid");
        setGridAttributes(upcomingGrid, "upcoming-grid");

        // add columns to todayGrid
        todayGrid.addComponentColumn(this::statusComboBox).setHeader("Status");
        todayGrid.addComponentColumn(this::estimatedCompletionTimePicker).setHeader("Estimated Completion");
        todayGrid.addEditColumn(Appointment::getStatusComments).text(Appointment::setStatusComments)
                .setHeader("Status Comments").setSortable(false).setResizable(true);

        // add columns to upcomingGrid
        upcomingGrid.addColumn(Appointment::getStatus).setHeader("Status").setSortable(false);
        upcomingGrid.addColumn(Appointment::getEstimatedCompletionTime).setHeader("Estimated Completion").setSortable(false);
        upcomingGrid.addColumn(Appointment::getStatusComments).setHeader("Status Comments").setResizable(true);

        // LISTENERS
        todayGrid.addItemPropertyChangedListener(e -> appointmentService.saveAppointment(e.getItem()));

        // LAYOUTS
        VerticalLayout todayLayout = new VerticalLayout(
                new H4("Today's Appointments"),
                noAppointmentsToday,
                todayGrid
        );
        setLayoutAttributes(todayLayout, "appointments-today");

        VerticalLayout upcomingLayout = new VerticalLayout(
                new H4("Upcoming Appointments"),
                noAppointmentsUpcoming,
                upcomingGrid
        );
        setLayoutAttributes(upcomingLayout, "upcoming-layout");

        // add both layouts to the class layout
        add(todayLayout, upcomingLayout);

        // populate data in the grid
        updateAppointmentsTodayGrid();
        updateUpcomingAppointmentsGrid();
    }

    /**
     * refreshes the appointments grid
     */
    private void updateAppointmentsTodayGrid() {
        if (appointmentService.findAllAppointmentsTodayByGarage(garage).isEmpty()) {
            todayGrid.setVisible(false);
            noAppointmentsToday.setVisible(true);
        } else
            todayGrid.setItems(appointmentService.findAllAppointmentsTodayByGarage(garage));
    }

    /**
     * refreshes the upcoming appointments grid
     */
    private void updateUpcomingAppointmentsGrid() {
        if (appointmentService.findAllUpcomingAppointmentsByGarage(garage).isEmpty()) {
            upcomingGrid.setVisible(false);
            noAppointmentsUpcoming.setVisible(true);
        } else
            upcomingGrid.setItems(appointmentService.findAllUpcomingAppointmentsByGarage(garage));
    }

    /**
     * sets common grid attributes and columns
     */
    private void setGridAttributes(GridPro<Appointment> grid, String className) {
        grid.setClassName(className);
        grid.removeAllColumns();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.addColumn(appointment -> FormattingUtils.SHORT_DATE_FORMATTER
                .format(appointment.getAppointmentDate())).setHeader("Date").setKey("appointmentDate")
                .setSortable(false).setResizable(true).setFlexGrow(1);
        grid.addColumn(appointment -> FormattingUtils.HOUR_FORMATTER
                .format(appointment.getAppointmentTime())).setHeader("Time").setKey("appointmentTime")
                .setSortable(false).setResizable(true).setFlexGrow(0);
        grid.addColumn(appointment -> getCarOwnerInfo(appointment.getVehicle())).setHeader("CarOwner")
                .setKey("carOwner").setSortable(false).setResizable(true);
        grid.addColumn(appointment -> getVehicleInfo(appointment.getVehicle())).setHeader("Vehicle").setKey("vehicle")
                .setSortable(false).setResizable(true);
        grid.addComponentColumn(this::viewHistory).setHeader("History").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0);
        grid.addColumn(Appointment::getCarOwnerComments).setHeader("Owner Comments").setSortable(false)
                .setKey("carOwnerComments").setResizable(true);
        grid.addComponentColumn(this::updateServicesButton).setHeader("Services")
                .setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0);

        grid.setHeightByRows(true);
    }

    /**
     * Concatenates the vehicle year, make and model for display in the grid
     * @param vehicle the vehicle to get info on
     * @return the concatenated string
     */
    private String getVehicleInfo(Vehicle vehicle) {
        return vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel();
    }

    /**
     * Concatenates the full name and phone number for the car owner
     * @param vehicle the vehicle the car owner is associated with
     * @return the concatenated string
     */
    private String getCarOwnerInfo(Vehicle vehicle) {
        CarOwner carOwner = vehicle.getCarOwner();
        return carOwner.getFullName() + " " + carOwner.getPhoneNumber();
    }

    /**
     * Sets common attributes for the layouts
     */
    private void setLayoutAttributes(VerticalLayout verticalLayout, String className) {
        verticalLayout.setClassName(className);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.getStyle().set("border", "1px solid #9E9E9E");
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
    }

    /**
     * Creates a timepicker for the estimated completion time field
     */
    private TimePicker estimatedCompletionTimePicker(Appointment appointment) {
        TimePicker timePicker = new TimePicker();
        timePicker.setLocale(Locale.US);
        timePicker.setStep(Duration.ofMinutes(30));
        timePicker.setMinTime(appointment.getAppointmentTime().plusMinutes(30));
        timePicker.setRequired(true);
        timePicker.setValue(appointment.getEstimatedCompletionTime());
        timePicker.addValueChangeListener(e -> {
            appointment.setEstimatedCompletionTime(timePicker.getValue());
            appointmentService.saveAppointment(appointment);
        });
        return timePicker;
    }

    /**
     * Creates a datepicker for the status field selection
     */
    private ComboBox<String> statusComboBox(Appointment appointment) {
        ComboBox<String> status = new ComboBox<>();
        status.setItems("Not Started", "In Progress", "Completed");
        status.setValue(appointment.getStatus());
        status.setRequired(true);
        status.addValueChangeListener(e -> {
            appointment.setStatus(status.getValue());
            appointmentService.saveAppointment(appointment);
            if(status.getValue().equals("Completed")){
                showGarageAppointmentDialog(appointment);
            }
        });
        return status;
    }

    /**
     * Creates the services icon button for each row in the grid
     *
     * @param appointment the appointment instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button updateServicesButton(Appointment appointment) {
        Button updateButton = new Button(VaadinIcon.WRENCH.create(), buttonClickEvent ->
                showGarageAppointmentDialog(appointment));
        updateButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return updateButton;
    }

    /**
     * Creates and opens a new GarageAppointmentDialog instance for viewing / editing the appointment services
     */
    private void showGarageAppointmentDialog(Appointment appointment) {
        garageEditApptServicesDialog = new GarageEditApptServicesDialog(appointmentService, appointment);
        garageEditApptServicesDialog.setWidth("50%");
        garageEditApptServicesDialog.setHeight("auto");
        garageEditApptServicesDialog.addListener(GarageEditApptServicesDialog.SaveSuccessEvent.class,
                this::onSave);
        garageEditApptServicesDialog.open();
    }

    /**
     * Fired when the AppointmentForm has been exited.
     *
     * @param event the event that fired this method
     */
    private void onSave(ComponentEvent<GarageEditApptServicesDialog> event) {
        garageEditApptServicesDialog.close();
        updateAppointmentsTodayGrid();
        updateUpcomingAppointmentsGrid();

        String successMessage = "The appointment services have been updated";
        Notification notification = new Notification(
                successMessage,
                4000,
                Notification.Position.TOP_END
        );
        notification.open();
    }

    /**
     * Creates the viewHistory icon button for each row in the grid
     *
     * @param appointment the appointment instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button viewHistory(Appointment appointment) {
        Button updateButton = new Button(VaadinIcon.CAR.create(), buttonClickEvent ->
                showVehicleHistoryDialog(appointment.getVehicle()));
        updateButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return updateButton;
    }

    /**
     * Creates and opens a new VehicleHistoryDialog instance for viewing the service history for a vehicle
     */
    private void showVehicleHistoryDialog(Vehicle vehicle) {
        VehicleHistoryDialog vehicleHistoryDialog = new VehicleHistoryDialog(vehicle, appointmentService);
        vehicleHistoryDialog.setWidth("75%");
        vehicleHistoryDialog.setHeight("auto");
        vehicleHistoryDialog.open();
    }
}
