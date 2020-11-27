package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.service.BusinessHoursService;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

/**
 * This class is a Vertical layout that controls the business hours for a given garage.
 */
public class GarageBusinessHoursView extends SplitLayout {
    private final Grid<BusinessHours> grid = new Grid<>(BusinessHours.class);
    BusinessHoursService businessHoursService;
    Garage garage;
    GarageBizHoursDialog garageBizHoursDialog;
    Button setCalendar = new Button("Set Calendar");
    Button updateCalendar = new Button("Update Calendar");
    DatePicker startDate = new DatePicker("Start Date");
    DatePicker endDate = new DatePicker("End Date");
    // this delete button is here for trouble shooting purposes only.
    // We don't want users to actually delete the hours, just set them to "Closed" if hours aren't needed on certain days
    // Button deleteHoursButton = new Button("Delete Business Hours");

    public GarageBusinessHoursView(BusinessHoursService businessHoursService,
                                   Garage garage) {
        this.businessHoursService = businessHoursService;
        this.garage = garage;

        // Sets the initial business hours for a garage the first time the first admin logs in
        if(businessHoursService.findByGarage(garage).isEmpty()) {
            businessHoursService.initializeBusinessHours(garage);
        }

        // deleteHoursButton.addClickListener(e -> deleteHours());

        // grid setup
        grid.addClassName("garage-business-hours-grid");
        grid.removeAllColumns();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // grid columns
        grid.addColumn(BusinessHours::getDayOfTheWeek).setHeader("Day").setKey("dayOfTheWeek").setSortable(false);
        grid.addColumn(businessHours -> convertBoolean(businessHours.getOpen()))
                .setHeader("Open").setKey("isOpen");
        //TODO fix date formatting to show AM/PM (unless we're good with 24 hours?)
        grid.addColumn(BusinessHours::getOpenTime).setHeader("Opening Time").setKey("openTime").setSortable(false);
        grid.addColumn(BusinessHours::getCloseTime).setHeader("Closing Time").setKey("closeTime").setSortable(false);
        grid.addComponentColumn(this::createUpdateButton).setHeader("Update").setTextAlign(ColumnTextAlign.CENTER);

        // create the layout for the business hours elements
        VerticalLayout bizHours = new VerticalLayout(
                new H4("Daily Business Hours"),
                grid
                //,deleteHoursButton
        );
        bizHours.setClassName("biz-hours");
        bizHours.setAlignItems(FlexComponent.Alignment.CENTER);
        bizHours.getStyle().set("border", "1px solid #9E9E9E");
        bizHours.setPadding(false);
        bizHours.setSpacing(false);

        // create the layout for the appointment calendar elements
        VerticalLayout appointmentCalendar = new VerticalLayout(
                new H4("Appointment Calendar Setup"),
                startDate,
                endDate,
                new HorizontalLayout(setCalendar, updateCalendar));
        appointmentCalendar.setClassName("appointment-calendar");
        appointmentCalendar.setAlignItems(FlexComponent.Alignment.CENTER);
        appointmentCalendar.getStyle().set("border", "1px solid #9E9E9E");
        appointmentCalendar.setPadding(false);
        appointmentCalendar.setSpacing(false);

        // this is a placeholder for Holidays and other dates the garage may be closed
        VerticalLayout datesClosed = new VerticalLayout();
        datesClosed.setClassName("dates-closed");
        datesClosed.setAlignItems(FlexComponent.Alignment.CENTER);
        datesClosed.getStyle().set("border", "1px solid #9E9E9E");
        datesClosed.setPadding(false);
        datesClosed.setSpacing(false);

        // create the child split layout for the appointmentCalendar and datesClosed layouts
        SplitLayout secondLayout = new SplitLayout();
        secondLayout.setOrientation(Orientation.HORIZONTAL);
        secondLayout.addToPrimary(appointmentCalendar);
       // secondLayout.addToSecondary(datesClosed);
        secondLayout.setSplitterPosition(50);

        // add the bizHours and child splitLayout to the parent splitLayout
        addToPrimary(bizHours);
        addToSecondary(secondLayout);
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

    //keep for troubleshooting
/*
    private void deleteHours(){
        businessHoursService.deleteBusinessHours(businessHoursService.findByGarage(garage));
        updateBusinessHoursList();
        deleteHoursButton.setEnabled(false);
    }
*/

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