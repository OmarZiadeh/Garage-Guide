package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.service.BusinessHoursService;
import com.TeamOne411.ui.view.garage.form.GarageBizHoursDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * This class is a Vertical layout that controls the business hours for a given garage.
 */
public class GarageBusinessHoursView extends HorizontalLayout {
    private final Grid<BusinessHours> grid = new Grid<>(BusinessHours.class);
    BusinessHoursService businessHoursService;
    Garage garage;
    GarageBizHoursDialog garageBizHoursDialog;
    //this delete button is here for trouble shooting purposes only.
    //We don't want users to actually delete the hours, just set them to "Closed" if hours aren't needed on certain days
 //   Button deleteHoursButton = new Button("Delete Business Hours");

    public GarageBusinessHoursView(BusinessHoursService businessHoursService,
                                   Garage garage) {
        this.businessHoursService = businessHoursService;
        this.garage = garage;

        // Sets the initial business hours for a garage the first time the first admin logs in
        if(businessHoursService.findByGarage(garage).isEmpty()) {
            businessHoursService.initializeBusinessHours(garage);
        }

        // Button listeners
       // deleteHoursButton.addClickListener(e -> deleteHours());

        grid.addClassName("garage-business-hours-grid");
        grid.setWidth("50%");
        grid.setHeightByRows(true);
        grid.removeAllColumns();

        grid.addColumn(BusinessHours::getDayOfTheWeek).setHeader("Day").setKey("dayOfTheWeek").setSortable(false);
        grid.addColumn(businessHours -> convertBoolean(businessHours.getOpen()))
                .setHeader("Open").setKey("isOpen");
        //TODO fix date formatting to show AM/PM (unless we're good with 24 hours?)
        grid.addColumn(BusinessHours::getOpenTime).setHeader("Opening Time").setKey("openTime").setSortable(false);
        grid.addColumn(BusinessHours::getCloseTime).setHeader("Closing Time").setKey("closeTime").setSortable(false);
        grid.addComponentColumn(this::createUpdateButton).setHeader("Update").setTextAlign(ColumnTextAlign.CENTER);

        // add the components to the vertical layout
        add(grid);                                      //deleteHoursButton
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