package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.service.BusinessHoursService;
import com.TeamOne411.backend.service.GarageCalendarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.Duration;

/**
 * This class is a Vertical layout that controls the business hours for a given garage.
 */
public class GarageBusinessHoursView extends VerticalLayout {
    private final Grid<BusinessHours> grid = new Grid<>(BusinessHours.class);
    BusinessHoursService businessHoursService;
    GarageCalendarService garageScheduleService;
    Garage garage;

    public GarageBusinessHoursView(BusinessHoursService businessHoursService,
                                   GarageCalendarService garageScheduleService,
                                   Garage garage) {
        this.businessHoursService = businessHoursService;
        this.garageScheduleService = garageScheduleService;
        this.garage = garage;

        Button setHoursButton = new Button("Set Business Hours");
        setHoursButton.setEnabled(businessHoursService.findByGarage(garage) == null);

        /* TODO: Add later once solution is determined for how booked appointments will be handled
        Button updateHoursButton = new Button("Update Business Hours");
        updateHoursButton.setEnabled(garageScheduleService.findByGarage(garage) != null);
        updateHoursButton.addClickListener(e -> updateHours());
         */

        Button deleteHoursButton = new Button("Delete Business Hours");
        deleteHoursButton.setEnabled(businessHoursService.findByGarage(garage) != null);

        // Button listeners
        setHoursButton.addClickListener(e -> {
            setInitialHours();
            setHoursButton.setEnabled(false);
            deleteHoursButton.setEnabled(true);
        });
        deleteHoursButton.addClickListener(e -> {
            deleteHours();
            deleteHoursButton.setEnabled(false);
            setHoursButton.setEnabled(true);
        });

        /* TODO: To be added later
        Button extendCalendarButton = new Button("Extend Appointment Calendar");
        extendCalendarButton.setEnabled(false);
        extendCalendarButton.addClickListener(e -> showExtendCalendarDialog());
         */

        grid.addClassName("garage-business-hours-grid");

        //TODO: Determine appropriate row height
        grid.setHeightByRows(true);
        grid.setWidth("75%");
        grid.removeAllColumns();

        //TODO: Fix string formatting
        grid.addColumn(BusinessHours::getDayOfTheWeek).setHeader("Day")
                .setSortable(false).setKey("dayOfTheWeek");

        // create the open or closed ComboBox component
        grid.addComponentColumn(this::createComboBox).setHeader("Open/Closed");
        //Add action listener for Open condition

        // create the open time TimePicker component
        grid.addComponentColumn(this::createOpenTimePicker).setHeader("Open Time");

        // create the close time TimePicker component
        grid.addComponentColumn(this::createCloseTimePicker).setHeader("Close Time");

        // add the components to the vertical layout
        add(new HorizontalLayout(setHoursButton, deleteHoursButton), // updateHoursButton, extendCalendarButton),
                grid);
        updateBusinessHoursList();
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateBusinessHoursList() {
        grid.setItems(businessHoursService.findByGarage(garage));
    }

    /**
     * Creates the open/closed combo box
     * @param businessHours the BusinessHours object the combo box is associated with
     * @return the ComboBox object
     */
    private ComboBox<String> createComboBox(BusinessHours businessHours){
        ComboBox<String> openClosedComboBox = new ComboBox<>();
        openClosedComboBox.setItems("Open", "Closed");
        if(businessHours.getOpen()){
            openClosedComboBox.setValue("Open");
        }
        else
            openClosedComboBox.setValue("Closed");

        return openClosedComboBox;
    }

    /**
     * Creates the TimePicker object for the opening time
     * @param businessHours the BusinessHours object the time picker is associated with
     * @return the TimePicker object
     */
    private TimePicker createOpenTimePicker(BusinessHours businessHours){
        TimePicker openTime = new TimePicker();
        openTime.setStep(Duration.ofMinutes(30));
        return openTime;
    }

    /**
     * Creates the TimePicker object for the closing time
     * @param businessHours the BusinessHours object the time picker is associated with
     * @return the TimePicker object
     */
    private TimePicker createCloseTimePicker(BusinessHours businessHours){
        TimePicker closeTime = new TimePicker();
        closeTime.setStep(Duration.ofMinutes(30));
        return closeTime;
    }

    private void setInitialHours(){
        businessHoursService.initializeBusinessHours(garage);
        updateBusinessHoursList();
    }

    private void deleteHours(){
        businessHoursService.deleteBusinessHours(businessHoursService.findByGarage(garage));
        updateBusinessHoursList();
    }
}