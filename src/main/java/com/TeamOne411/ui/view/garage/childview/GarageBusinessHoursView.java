package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.entity.schedule.ClosedDate;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import com.TeamOne411.backend.service.BusinessHoursService;
import com.TeamOne411.backend.service.GarageCalendarService;
import com.TeamOne411.ui.utils.FormattingUtils;
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
    private final Grid<ClosedDate> excDatesGrid = new Grid<>(ClosedDate.class);
    private final BusinessHoursService businessHoursService;
    private final GarageCalendarService garageCalendarService;
    private final GarageCalendar garageCalendar;
    private final Garage garage;
    private final Button saveCalButton = new Button("Generate Calendar");
    private final DatePicker startDatePicker = new DatePicker("Calendar Start Date");
    private final Button saveExcDateButton = new Button("Add Closed Date");
    private final DatePicker excDatePicker = new DatePicker("Garage Will Be Closed On");
    private GarageBizHoursDialog garageBizHoursDialog;
    private ClosedDate closedDate;

    public GarageBusinessHoursView(BusinessHoursService businessHoursService,
                                   GarageCalendarService garageCalendarService,
                                   Garage garage) {
        this.businessHoursService = businessHoursService;
        this.garage = garage;
        this.garageCalendarService = garageCalendarService;

        // Sets the initial business hours for a garage the first time the first admin for the garage logs in
        if (businessHoursService.findByGarage(garage).isEmpty()) {
            businessHoursService.initializeBusinessHours(garage);
        }

        // Checks if a garageCalendar exists already, sets the calendar if one does exist
        // otherwise creates a new garageCalendar instance if one doesn't exist
        if (garageCalendarService.findCalendarByGarage(garage) != null) {
            garageCalendar = garageCalendarService.findCalendarByGarage(garage);
            startDatePicker.setEnabled(false);
        } else {
            garageCalendar = new GarageCalendar();
            garageCalendar.setGarage(garage);
        }

        // BUSINESS HOURS
        // setup the business hours grid
        bizHoursGrid.addClassName("garage-business-hours-grid");
        bizHoursGrid.removeAllColumns();
        bizHoursGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        bizHoursGrid.addColumn(BusinessHours::getDayOfTheWeek).setHeader("Day")
                .setKey("dayOfTheWeek").setSortable(false);
        bizHoursGrid.addColumn(businessHours -> convertBoolean(businessHours.getOpen()))
                .setHeader("Open").setKey("isOpen");
        bizHoursGrid.addColumn(businessHours -> FormattingUtils.convertTime(businessHours.getOpenTime()))
                .setHeader("Opening Time").setKey("openTime").setSortable(false);
        bizHoursGrid.addColumn(businessHours -> FormattingUtils.convertTime(businessHours.getCloseTime()))
                .setHeader("Closing Time").setKey("closeTime").setSortable(false);
        bizHoursGrid.addComponentColumn(this::createUpdateButton).setHeader("Update")
                .setTextAlign(ColumnTextAlign.CENTER);

        // APPOINTMENT CALENDAR SETUP
        // disable saveCalendar button until calendar changes have been made
        saveCalButton.setEnabled(false);

        // setup appointment calendar datepicker
        startDatePicker.setRequired(true);
        startDatePicker.setRequiredIndicatorVisible(true);
        startDatePicker.setValue(garageCalendar.getCalendarStartDate());
        if (startDatePicker.getValue() == null) {
            startDatePicker.setMin(LocalDate.now());
            startDatePicker.setMax(LocalDate.now().plusMonths(6));
        }

        // CLOSED DATES
        // setup exceptionsDatesGrid
        excDatesGrid.addClassName("garage-closed-dates-grid");
        excDatesGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        excDatesGrid.setHeightByRows(true);
        excDatesGrid.removeAllColumns();
        excDatesGrid.addColumn(closedDate -> FormattingUtils.SHORT_DATE_FORMATTER.format(closedDate.getNotOpenDate()))
                .setHeader("Closed Date").setKey("notOpenDate");
        excDatesGrid.addComponentColumn(this::exceptionUpdateButton)
                .setHeader("Update").setTextAlign(ColumnTextAlign.CENTER);
        excDatesGrid.addComponentColumn(this::exceptionDeleteButton).setHeader("Delete")
                .setTextAlign(ColumnTextAlign.CENTER);

        // set attributes for exception date picker and exception save button
        excDatePicker.setMin(LocalDate.now());
        saveExcDateButton.setEnabled(false);

        // LISTENERS
        startDatePicker.addValueChangeListener(e -> {
            // do not allow a null value to be set on the garageCalendar instance, otherwise set what was specified
            if (startDatePicker.getValue() != null) {
                garageCalendar.setCalendarStartDate(startDatePicker.getValue());
                //set the calendar end date to be 3 months from the specified start date
                //TODO enable a rolling update to extend the end date by 1 day daily (ICEBOX)
                garageCalendar.setCalendarEndDate(startDatePicker.getValue().plusMonths(3));
                saveCalButton.setEnabled(true);
            }
        });
        saveCalButton.addClickListener(e -> saveCalendar());
        excDatePicker.addValueChangeListener(e -> saveExcDateButton.setEnabled(true));
        saveExcDateButton.addClickListener(e -> saveDate());

        // LAYOUTS
        // create the layout for the business hours elements
        VerticalLayout bizHoursLayout = new VerticalLayout(
                new H4("Daily Business Hours"),
                bizHoursGrid
        );
        bizHoursLayout.setClassName("biz-hours");
        setLayoutAttributes(bizHoursLayout);

        // create the layout for the appointment calendar elements
        VerticalLayout apptCalLayout = new VerticalLayout(
                new H4("Appointment Calendar Setup"),
                startDatePicker,
                saveCalButton
        );
        apptCalLayout.setClassName("appointment-calendar");
        setLayoutAttributes(apptCalLayout);

        // create the layout for exception dates
        VerticalLayout excDatesLayout = new VerticalLayout(
                new H4("Holidays/Dates Closed"),
                excDatePicker,
                saveExcDateButton,
                excDatesGrid
        );
        setLayoutAttributes(excDatesLayout);

        // create a child SplitLayout for the appointmentCalendar and datesClosed layouts
        SplitLayout childLayout = new SplitLayout();
        childLayout.setOrientation(Orientation.HORIZONTAL);
        childLayout.addToPrimary(excDatesLayout);
        childLayout.addToSecondary(apptCalLayout);
        childLayout.setSplitterPosition(60);

        // add the bizHours and child splitLayout to the parent splitLayout
        addToPrimary(bizHoursLayout);
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
        excDatesGrid.setItems(garageCalendarService.findClosedDatesByGarage(garage));
    }

    /**
     * Sets common attributes for the layouts
     */
    private void setLayoutAttributes(VerticalLayout verticalLayout) {
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.getStyle().set("border", "1px solid #9E9E9E");
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
    }

    /**
     * Saves the new/updated garageCalendar to the database and generates available timeSlots
     */
    private void saveCalendar() {
        garageCalendarService.saveGarageCalendar(garageCalendar);

        // disable the calendar setup so the user cannot make changes (for now)
        saveCalButton.setEnabled(false);
        startDatePicker.setEnabled(false);

        // generate appointment time slots via async thread
        garageCalendarService.generateTimeSlots(garageCalendar, businessHoursService);
    }

    /**
     * Saves the closedDate to the database
     */
    private void saveDate() {
        if (closedDate == null) {
            closedDate = new ClosedDate();
            closedDate.setGarage(garage);
        }
        closedDate.setNotOpenDate(excDatePicker.getValue());
        garageCalendarService.saveClosedDate(closedDate);
        closedDate = null;
        excDatePicker.setValue(null);
        saveExcDateButton.setText("Add Closed Date");
        saveExcDateButton.setEnabled(false);
        updateDatesClosedList();
    }


    /**
     * Converts the isOpen Boolean so it displays as something logical to the customer
     * instead of "true" or "false"
     *
     * @param isOpen boolean indicating if the garage is open that day of the week
     * @return string conversion of the boolean
     */
    private String convertBoolean(Boolean isOpen) {
        return isOpen ? "Yes" : "No";
    }

    /**
     * Creates the update icon button for each row in the grid
     *
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
     *
     * @param closedDate the ClosedDate instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button exceptionUpdateButton(ClosedDate closedDate) {
        Button updateButton = new Button(VaadinIcon.EDIT.create(), buttonClickEvent -> {
            excDatePicker.setValue(closedDate.getNotOpenDate());
            saveExcDateButton.setText("Update Closed Date");
            this.closedDate = closedDate;
        });
        updateButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return updateButton;
    }

    /**
     * Creates the delete icon button for each row in the grid
     *
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
     *
     * @param event the event that fired this method
     */
    private void onSave(ComponentEvent<GarageBizHoursDialog> event) {
        garageBizHoursDialog.close();
        updateBusinessHoursList();
    }
}