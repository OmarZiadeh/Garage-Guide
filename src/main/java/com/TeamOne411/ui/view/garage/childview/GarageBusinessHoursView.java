package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import com.TeamOne411.backend.service.BusinessHoursService;
import com.TeamOne411.backend.service.GarageCalendarService;
import com.TeamOne411.ui.view.garage.form.GarageBizHoursDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import java.time.LocalDate;

/**
 * This class is a splite layout that controls the business hours and appointment calendar for the garage
 */
public class GarageBusinessHoursView extends SplitLayout {
    private final Grid<BusinessHours> grid = new Grid<>(BusinessHours.class);
    BusinessHoursService businessHoursService;
    GarageCalendarService garageCalendarService;
    GarageCalendar garageCalendar;
    Garage garage;
    GarageBizHoursDialog garageBizHoursDialog;
    Button saveCalendar = new Button();
    DatePicker startDate = new DatePicker("Start Date");
    DatePicker endDate = new DatePicker("End Date");
    VerticalLayout bizHours;
    VerticalLayout appointmentCalendar;
    VerticalLayout datesClosed;
    LocalDate previousStartDate;
    LocalDate previousEndDate;

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
        if(garageCalendarService.findByGarage(garage) == null){
            garageCalendar = new GarageCalendar();
            garageCalendar.setGarage(garage);
            saveCalendar.setText("Generate Calendar");
            // TODO remove later
            deleteButton.setEnabled(false);
        }
        else {
            garageCalendar = garageCalendarService.findByGarage(garage);
            saveCalendar.setText("Update Calendar");
        }

        // business hours grid setup
        grid.addClassName("garage-business-hours-grid");
        grid.removeAllColumns();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // business hours grid columns
        grid.addColumn(BusinessHours::getDayOfTheWeek).setHeader("Day").setKey("dayOfTheWeek").setSortable(false);
        grid.addColumn(businessHours -> convertBoolean(businessHours.getOpen()))
                .setHeader("Open").setKey("isOpen");
        //TODO fix date formatting to show AM/PM (unless we're good with 24 hours?)
        grid.addColumn(BusinessHours::getOpenTime).setHeader("Opening Time").setKey("openTime").setSortable(false);
        grid.addColumn(BusinessHours::getCloseTime).setHeader("Closing Time").setKey("closeTime").setSortable(false);
        grid.addComponentColumn(this::createUpdateButton).setHeader("Update").setTextAlign(ColumnTextAlign.CENTER);

        // disable saveCalendar button until calendar changes have been made
        saveCalendar.setEnabled(false);

        // appointment calendar DatePicker setups
        setDateAttributes(startDate);
        setDateAttributes(endDate);
        previousStartDate = garageCalendar.getCalendarStartDate();
        startDate.setValue(previousStartDate);
        previousEndDate = garageCalendar.getCalendarEndDate();
        endDate.setValue(previousEndDate);

        if(startDate.getValue() == null){
            startDate.setMin(LocalDate.now());
            endDate.setMin(LocalDate.now().plusDays(1));
        }

        // startDate listener
        startDate.addValueChangeListener(e -> {
            // do not allow a null value to be set on the garageCalendar instance, otherwise set what has been provided
            if(startDate.getValue() != null) {
                garageCalendar.setCalendarStartDate(startDate.getValue());
                endDate.setMin(startDate.getValue().plusDays(1));
                if(endDate.getValue() != null){
                    saveCalendar.setEnabled(true);
                }
            }
        });
        // endDate listener
        endDate.addValueChangeListener(e -> {
            // do not allow a null value to be set on the garageCalendar instance, otherwise set what has been provided
            if(endDate.getValue() != null) {
                garageCalendar.setCalendarEndDate(endDate.getValue());
                startDate.setMax(endDate.getValue().minusDays(1));
                if(startDate.getValue() != null){
                    saveCalendar.setEnabled(true);
                }
            }
        });

        // save calendar button listener
        saveCalendar.addClickListener(e -> saveCalendar());

        // TODO: remove this later. Keep temporarily for troubleshooting
        deleteButton.addClickListener(e -> delete());

        // create the layout for the business hours elements
        bizHours = new VerticalLayout(
                new H4("Daily Business Hours"),
                grid
        );
        bizHours.setClassName("biz-hours");
        setLayoutAttributes(bizHours);

        // create the layout for the appointment calendar elements
        appointmentCalendar = new VerticalLayout(
                new H4("Appointment Calendar Setup"),
                startDate,
                endDate,
                saveCalendar,
                deleteButton);
        appointmentCalendar.setClassName("appointment-calendar");
        setLayoutAttributes(appointmentCalendar);

        // this is a placeholder for Holidays and other dates when the garage may be closed
        datesClosed = new VerticalLayout();
        setLayoutAttributes(datesClosed);

        // create a child SplitLayout for the appointmentCalendar and datesClosed layouts
        SplitLayout childLayout = new SplitLayout();
        childLayout.setOrientation(Orientation.HORIZONTAL);
        childLayout.addToPrimary(appointmentCalendar);
       // childLayout.addToSecondary(datesClosed);
        childLayout.setSplitterPosition(50);

        // add the bizHours and child splitLayout to the parent splitLayout
        addToPrimary(bizHours);
        addToSecondary(childLayout);
        setSplitterPosition(50);

        // populate the business hours grid
        updateBusinessHoursList();
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateBusinessHoursList() {
        grid.setItems(businessHoursService.findByGarage(garage));
    }

    /**
     * Sets common attributes for the appointment calendar DatePicker objects
     */
    private void setDateAttributes(DatePicker datePicker){
        datePicker.setRequired(true);
        datePicker.setRequiredIndicatorVisible(true);
        datePicker.setMax(LocalDate.now().plusYears(1));
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

    // TODO: remove this later. Keep temporarily for troubleshooting
    private void delete(){
        garageCalendarService.deleteGarageCalendar(garageCalendarService.findByGarage(garage));
        startDate.setValue(null);
        endDate.setValue(null);
        deleteButton.setEnabled(false);
        saveCalendar.setEnabled(false);
        previousStartDate = null;
        previousEndDate = null;
    }

    /**
     * Saves the new/updated garageCalendar to the database
     */
    private void saveCalendar() {
        garageCalendarService.saveGarageCalendar(garageCalendar);
        saveCalendar.setEnabled(false);
        deleteButton.setEnabled(true);

        if(previousStartDate == null){
            garageCalendarService.generateTimeSlots(garageCalendar, businessHoursService);
        }
        //TODO add logic for updating time slots if an existing calendar was revised
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