package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.service.AppointmentService;
import com.TeamOne411.ui.utils.FormattingUtils;
import com.TeamOne411.ui.view.garage.form.GarageAppointmentDialog;
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

public class GarageAppointmentsView extends VerticalLayout {
    private final AppointmentService appointmentService;
    private final Garage garage;
    private final GridPro<Appointment> todayGrid = new GridPro<>(Appointment.class);
    private final GridPro<Appointment> upcomingGrid = new GridPro<>(Appointment.class);
    private final H5 noAppointmentsToday = new H5("Your garage does not have any appointments scheduled for today");
    private final H5 noAppointmentsUpcoming = new H5("Your garage does not have any upcoming appointments scheduled");
    private GarageAppointmentDialog garageAppointmentDialog;

    public GarageAppointmentsView(AppointmentService appointmentService, Garage garage) {
        this.appointmentService = appointmentService;
        this.garage = garage;

        // H5 message setup
        noAppointmentsToday.setVisible(false);
        noAppointmentsUpcoming.setVisible(false);

        // GRID SETUP
        setGridAttributes(todayGrid, "today-grid");
        setGridAttributes(upcomingGrid, "upcoming-grid");

        // TODO add columns for car owner info
        // add columns to todayGrid
        todayGrid.addComponentColumn(this::statusComboBox).setHeader("Status");
        todayGrid.addComponentColumn(this::estimatedCompletionTimePicker).setHeader("Estimated Completion");
        todayGrid.addEditColumn(Appointment::getStatusComments).text(Appointment::setStatusComments)
                .setHeader("Status Comments").setSortable(false).setFlexGrow(2);

        // add columns to upcomingGrid
        upcomingGrid.addColumn(Appointment::getStatus).setHeader("Status").setSortable(false);
        upcomingGrid.addColumn(Appointment::getEstimatedCompletionTime).setHeader("Estimated Completion").setSortable(false);
        upcomingGrid.addColumn(Appointment::getStatusComments).setHeader("Status Comments").setFlexGrow(2);

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
     * refreshes the future appointments grid
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
                .format(appointment.getAppointmentDate())).setHeader("Date").setKey("appointmentDate").setSortable(false);
        grid.addColumn(appointment -> FormattingUtils.HOUR_FORMATTER
                .format(appointment.getAppointmentTime())).setHeader("Time").setKey("appointmentTime").setSortable(false);
        grid.addColumn(Appointment::getCarOwnerComments).setHeader("Owner Comments").setSortable(false)
                .setKey("carOwnerComments").setFlexGrow(2);
        grid.addComponentColumn(this::updateServicesButton).setHeader("Services")
                .setTextAlign(ColumnTextAlign.CENTER);

        grid.setHeightByRows(true);
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
     * Creates the view icon button for each row in the grid
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
     * Creates and opens a new GarageApptDialog instance for viewing / editing the appointment services
     */
    private void showGarageAppointmentDialog(Appointment appointment) {
        garageAppointmentDialog = new GarageAppointmentDialog(appointmentService, appointment);
        garageAppointmentDialog.setWidth("50%");
        garageAppointmentDialog.setHeight("auto");
        garageAppointmentDialog.addListener(GarageAppointmentDialog.SaveSuccessEvent.class,
                this::onSave);
        garageAppointmentDialog.open();
    }

    /**
     * Fired when the AppointmentForm has been exited.
     *
     * @param event the event that fired this method
     */
    private void onSave(ComponentEvent<GarageAppointmentDialog> event) {
        garageAppointmentDialog.close();
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
}
