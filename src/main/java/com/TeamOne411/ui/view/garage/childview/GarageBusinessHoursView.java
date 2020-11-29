package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.entity.schedule.ClosedDate;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import com.TeamOne411.backend.service.BusinessHoursService;
import com.TeamOne411.backend.service.GarageCalendarService;
import com.TeamOne411.ui.view.garage.form.GarageBizHoursDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import java.time.LocalDate;

/**
 * This class is a split layout that controls the business hours and appointment calendar for the garage
 */
public class GarageBusinessHoursView extends SplitLayout {
    private final Grid<BusinessHours> bizHoursGrid = new Grid<>(BusinessHours.class);
    BusinessHoursService businessHoursService;
    GarageCalendarService garageCalendarService;
    GarageCalendar garageCalendar;
    Garage garage;
    GarageBizHoursDialog garageBizHoursDialog;
    Button saveCalendar = new Button("Generate Calendar");
    DatePicker startDate = new DatePicker("Calendar Start Date");
    VerticalLayout bizHours;
    VerticalLayout appointmentCalendar;
    VerticalLayout exceptionDates;
    Button saveExceptionDate = new Button("Add Closed Date");
    DatePicker exceptionDatePicker = new DatePicker("Garage Will Be Closed On");
    ClosedDate closedDate;
    private final Grid<ClosedDate> exceptionDatesGrid = new Grid<>(ClosedDate.class);

    // TODO: remove this later. Keep temporarily for troubleshooting
    Button deleteButton = new Button("Delete Calendar");

    public GarageBusinessHoursView(BusinessHoursService businessHoursService,
                                   GarageCalendarService garageCalendarService,
                                   Garage garage) {
        this.businessHoursService = businessHoursService;
        this.garage = garage;
        this.garageCalendarService = garageCalendarService;

        // Sets the initial business hours for a garage the first time the first admin for the garage logs in
        if(businessHoursService.findByGarage(garage).isEmpty()) {
            businessHoursService.initializeBusinessHours(garage);
        }

        // Checks if a garageCalendar exists already, creates one if it doesn't exist
        if(garageCalendarService.findCalendarByGarage(garage) != null){
            garageCalendar = garageCalendarService.findCalendarByGarage(garage);
            startDate.setEnabled(false);
        }
        else {
            garageCalendar = new GarageCalendar();
            garageCalendar.setGarage(garage);
            startDate.setAutoOpen(true);
            // TODO remove later
            deleteButton.setEnabled(false);
        }
        // TODO remove later
        deleteButton.setVisible(false);

        //BUSINESS HOURS
        // business hours grid setup
        bizHoursGrid.addClassName("garage-business-hours-grid");
        bizHoursGrid.removeAllColumns();
        bizHoursGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        bizHoursGrid.addColumn(BusinessHours::getDayOfTheWeek).setHeader("Day").setKey("dayOfTheWeek").setSortable(false);
        bizHoursGrid.addColumn(businessHours -> convertBoolean(businessHours.getOpen())).setHeader("Open").setKey("isOpen");
        //TODO fix date formatting to show AM/PM (unless we're good with 24 hours?)
        bizHoursGrid.addColumn(BusinessHours::getOpenTime).setHeader("Opening Time").setKey("openTime").setSortable(false);
        bizHoursGrid.addColumn(BusinessHours::getCloseTime).setHeader("Closing Time").setKey("closeTime").setSortable(false);
        bizHoursGrid.addComponentColumn(this::createUpdateButton).setHeader("Update").setTextAlign(ColumnTextAlign.CENTER);

        // APPOINTMENT CALENDAR SETUP
        // disable saveCalendar button until calendar changes have been made
        saveCalendar.setEnabled(false);

        // appointment calendar DatePicker setups
        startDate.setRequired(true);
        startDate.setRequiredIndicatorVisible(true);
        startDate.setValue(garageCalendar.getCalendarStartDate());
        if(startDate.getValue() == null){
            startDate.setMin(LocalDate.now());

            //Do not allow calendar to start later than 6 months out
            startDate.setMax(LocalDate.now().plusMonths(6));
        }

        // startDate listener
        startDate.addValueChangeListener(e -> {
            // do not allow a null value to be set on the garageCalendar instance, otherwise set what was specified
            if(startDate.getValue() != null) {
                garageCalendar.setCalendarStartDate(startDate.getValue());
                //set the calendar end date to be 3 months from the specified start date
                //TODO adjust end value to 3 months (temporarily set to 2 weeks for testing)
                //TODO enable a rolling update to extend the end date by 1 day daily
                garageCalendar.setCalendarEndDate(startDate.getValue().plusWeeks(2));
                saveCalendar.setEnabled(true);
            }
        });

        // save calendar button listener
        saveCalendar.addClickListener(e -> saveCalendar());
        // TODO: remove this later. Keep temporarily for troubleshooting
        deleteButton.addClickListener(e -> delete());

        // CLOSED/EXCEPTION DATES
        // set attributes for exceptionsDatesGrid
        exceptionDatesGrid.addClassName("garage-closed-dates-grid");
        exceptionDatesGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        exceptionDatesGrid.setHeightByRows(true);
        exceptionDatesGrid.removeAllColumns();
        exceptionDatesGrid.addColumn(ClosedDate::getNotOpenDate).setHeader("Closed Date").setKey("notOpenDate");
        exceptionDatesGrid.addComponentColumn(this::exceptionUpdateButton)
                .setHeader("Update").setTextAlign(ColumnTextAlign.CENTER);
        exceptionDatesGrid.addComponentColumn(this::exceptionDeleteButton).setHeader("Delete")
                .setTextAlign(ColumnTextAlign.CENTER);

        // attributes for exception button and date picker
        exceptionDatePicker.setMin(LocalDate.now());
        saveExceptionDate.setEnabled(false);
        // exception date listeners
        exceptionDatePicker.addValueChangeListener(e -> saveExceptionDate.setEnabled(true));
        saveExceptionDate.addClickListener(e -> saveDate());

        // LAYOUTS
        // create the layout for the business hours elements
        bizHours = new VerticalLayout(
                new H4("Daily Business Hours"),
                bizHoursGrid
        );
        bizHours.setClassName("biz-hours");
        setLayoutAttributes(bizHours);

        // create the layout for the appointment calendar elements
        appointmentCalendar = new VerticalLayout(
                new H4("Appointment Calendar Setup"),
                startDate,
                saveCalendar,
                //TODO: remove this later. Keep temporarily for troubleshooting
                deleteButton
        );
        appointmentCalendar.setClassName("appointment-calendar");
        setLayoutAttributes(appointmentCalendar);

        // create the layout for exception dates
        exceptionDates = new VerticalLayout(
                new H4("Holidays/Dates Closed"),
                exceptionDatePicker,
                saveExceptionDate,
                exceptionDatesGrid
        );
        setLayoutAttributes(exceptionDates);

        // create a child SplitLayout for the appointmentCalendar and datesClosed layouts
        SplitLayout childLayout = new SplitLayout();
        childLayout.setOrientation(Orientation.HORIZONTAL);
        childLayout.addToPrimary(exceptionDates);
        childLayout.addToSecondary(appointmentCalendar);
        childLayout.setSplitterPosition(60);

        // add the bizHours and child splitLayout to the parent splitLayout
        addToPrimary(bizHours);
        addToSecondary(childLayout);
        setSplitterPosition(50);

        // POPULATE DATA
        // populate the grids
        updateBusinessHoursList();
        updateDatesClosedList();
    }

    /**
     * Refreshes the business hours grid list from the database
     */
    private void updateBusinessHoursList() {
        bizHoursGrid.setItems(businessHoursService.findByGarage(garage));
    }

    /**
     * Refreshes the exception dates grid from the database
     */
    private void updateDatesClosedList() {
        exceptionDatesGrid.setItems(garageCalendarService.findClosedDatesByGarageOrderByNotOpenDate(garage));
    }

    /**
     * Sets common attributes for the layouts
     */
    private void setLayoutAttributes(VerticalLayout verticalLayout){
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.getStyle().set("border", "1px solid #9E9E9E");
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
    }

    // TODO: remove this method later. Keep temporarily for troubleshooting
    private void delete(){
        garageCalendarService.deleteGarageCalendar(garageCalendarService.findCalendarByGarage(garage));
        startDate.setValue(null);
        startDate.setEnabled(true);
        saveCalendar.setEnabled(false);
    }

    /**
     * Saves the new/updated garageCalendar to the database and generates available timeSlots
     */
    private void saveCalendar() {
        garageCalendarService.saveGarageCalendar(garageCalendar);

        // disable the calendar setup so the user cannot make changes (for now)
        saveCalendar.setEnabled(false);
        startDate.setEnabled(false);

        // generate appointment time slots via async thread
        garageCalendarService.generateTimeSlots(garageCalendar, businessHoursService);

        //TODO: remove this later. keep temporarily for troubleshooting
        deleteButton.setEnabled(true);
    }

    /**
     * Saves the closedDate to the database
     */
    private void saveDate(){
        if(closedDate == null){
            closedDate = new ClosedDate();
            closedDate.setGarage(garage);
        }
        closedDate.setNotOpenDate(exceptionDatePicker.getValue());
        garageCalendarService.saveClosedDate(closedDate);
        closedDate = null;
        exceptionDatePicker.setValue(null);
        saveExceptionDate.setText("Add Closed Date");
        saveExceptionDate.setEnabled(false);
        updateDatesClosedList();
    }

    /**
     * Converts the isOpen Boolean so it displays as something logical to the customer
     * instead of "true" or "false"
     * @param isOpen boolean indicating if the garage is open that day of the week
     * @return string conversion of the boolean
     */
    private String convertBoolean(Boolean isOpen){
        return isOpen ? "Yes" : "No";
    }

    /**
     * Creates the update icon button for each row in the grid
     * @param businessHours the businessHours instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button createUpdateButton(BusinessHours businessHours) {
        Button updateButton = new Button(VaadinIcon.EDIT.create(), buttonClickEvent ->
                showBusinessHoursDialog(businessHours));
        updateButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return updateButton;
    }

    /**
     * Creates the update icon button for each row in the grid
     * @param closedDate the ClosedDate instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button exceptionUpdateButton(ClosedDate closedDate) {
        Button updateButton = new Button(VaadinIcon.EDIT.create(), buttonClickEvent -> {
            exceptionDatePicker.setValue(closedDate.getNotOpenDate());
            saveExceptionDate.setText("Update Closed Date");
            this.closedDate = closedDate;
        });
        updateButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return updateButton;
    }

    /**
     * Creates the delete icon button for each row in the grid
     * @param closedDate the ClosedDate instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button exceptionDeleteButton(ClosedDate closedDate) {
        Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create(), buttonClickEvent ->
                deleteClosedDateClick(closedDate));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return deleteButton;
    }

    /**
     * Fired on deleteClosedDateClick(). Shows a confirm dialog and then deletes the selected date
     */
    private void deleteClosedDateClick(ClosedDate closedDate) {

        String message = "Are you sure you want to delete this Date?";
        ConfirmDialog confirmDeleteDialog = new ConfirmDialog(
                "Delete Date", message,
                "Delete",
                e -> onDeleteConfirm(closedDate),
                "Cancel",
                e -> e.getSource().close());

        confirmDeleteDialog.setConfirmButtonTheme("error primary");
        confirmDeleteDialog.open();
    }

    /**
     * Fired when delete confirm dialog is confirmed by user. Deletes selected ClosedDate.
     */
    private void onDeleteConfirm(ClosedDate closedDate) {
        if (closedDate != null) {
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
    }

    /**
     * Creates and opens a new GarageBizHoursDialog instance for updating the Business Hours
     */
    private void showBusinessHoursDialog(BusinessHours businessHours) {
        garageBizHoursDialog = new GarageBizHoursDialog(businessHoursService, businessHours);
        garageBizHoursDialog.setWidth("25%");
        garageBizHoursDialog.setHeight("auto");
        garageBizHoursDialog.addListener(GarageBizHoursDialog.SaveSuccessEvent.class,
                this::onSave);
        garageBizHoursDialog.open();
    }

    /**
     * Fired when the GarageBizHoursForm has been exited. Refreshes the business hours grid.
     * @param event the event that fired this method
     */
    private void onSave(ComponentEvent<GarageBizHoursDialog> event) {
        garageBizHoursDialog.close();
        updateBusinessHoursList();
    }
}