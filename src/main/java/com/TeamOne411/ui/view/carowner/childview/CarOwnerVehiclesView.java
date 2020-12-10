
package com.TeamOne411.ui.view.carowner.childview;


import com.TeamOne411.backend.entity.Vehicle;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.service.AppointmentService;
import com.TeamOne411.backend.service.VehicleService;
import com.TeamOne411.backend.service.api.car.ApiVehicleService;
import com.TeamOne411.ui.view.carowner.form.VehicleEditorDialog;
import com.TeamOne411.ui.view.carowner.form.VehicleHistoryDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Optional;

/**
 * This class is a Vertical layout that shows a list of Vehicles and gives the ability to add/edit/delete them.
 */

public class CarOwnerVehiclesView extends VerticalLayout {

    private final Grid<Vehicle> grid = new Grid<>(Vehicle.class);
    private final VehicleService vehicleService;
    private final CarOwner loggedInCarOwner;
    private final ApiVehicleService apiVehicleService;
    private final AppointmentService appointmentService;
    private VehicleEditorDialog vehicleEditorDialog;

    public CarOwnerVehiclesView(
            VehicleService vehicleService,
            ApiVehicleService apiVehicleService,
            CarOwner loggedInCarOwner,
            AppointmentService appointmentService
    ) {
        this.vehicleService = vehicleService;
        this.loggedInCarOwner = loggedInCarOwner;
        this.apiVehicleService = apiVehicleService;
        this.appointmentService = appointmentService;

        // configure the Vehicle grid
        grid.addClassName("vehicle-grid");
        grid.setHeightByRows(true);
        grid.setMaxHeight("25vh");
        grid.setColumns("year", "make", "model", "vin");
        grid.addComponentColumn(this::viewHistory).setHeader("Service History").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        Button addVehicleButton = new Button("Add New Vehicle");
        addVehicleButton.addClickListener(e -> showNewVehicleDialog());

        Button editVehicleButton = new Button("Edit Vehicle");
        editVehicleButton.addClickListener(e -> showEditVehicleDialog());
        editVehicleButton.setEnabled(false);

        Button deleteVehicleButton = new Button("Delete Vehicle");
        deleteVehicleButton.addClickListener(e -> deleteVehicleClick());
        deleteVehicleButton.setEnabled(false);

        grid.addSelectionListener(e -> {
            editVehicleButton.setEnabled(!grid.getSelectedItems().isEmpty());
            deleteVehicleButton.setEnabled(!grid.getSelectedItems().isEmpty());
        });

        add(
                new HorizontalLayout(addVehicleButton, editVehicleButton, deleteVehicleButton),
                grid
        );

        updateVehicleList();
    }

    /**
     * Calls the VehicleService to refresh the list of Vehicles. Call this anytime the Vehicles may have been edited.
     */

    private void updateVehicleList() {
        grid.setItems(vehicleService.findByCarOwner(loggedInCarOwner));
    }


    /**
     * Creates and opens a new VehicleEditorDialog instance not in edit mode (to register new Vehicle)
     */

    private void showNewVehicleDialog() {
        vehicleEditorDialog = new VehicleEditorDialog(loggedInCarOwner, apiVehicleService, vehicleService);

        vehicleEditorDialog.setWidth("250px");
        vehicleEditorDialog.setWidth("750px");

        vehicleEditorDialog.addListener(VehicleEditorDialog.AddVehicleSuccessEvent.class, this::onVehicleRegisteredSuccess);

        vehicleEditorDialog.open();
    }


    /**
     * Gets the selected Vehicle from the grid, passes it to a new VehicleEditorDialog instance and opens it in Edit Mode.
     */

    private void showEditVehicleDialog() {
        Optional<Vehicle> selectedVehicle = grid.getSelectedItems().stream().findFirst();

        if (selectedVehicle.isPresent()) {
            vehicleEditorDialog = new VehicleEditorDialog(selectedVehicle.get(), apiVehicleService, vehicleService);

            vehicleEditorDialog.setWidth("250px");
            vehicleEditorDialog.setWidth("750px");

            vehicleEditorDialog.addListener(VehicleEditorDialog.EditVehicleSuccessEvent.class, this::onVehicleEditedSuccess);

            vehicleEditorDialog.open();
        }
    }


    /**
     * Fired on deleteVehicleButton click. Shows a confirm dialog and then deletes the selected Vehicle.
     */

    private void deleteVehicleClick() {
        Optional<Vehicle> selectedVehicle = grid.getSelectedItems().stream().findFirst();

        if (selectedVehicle.isPresent()) {
            String message = "Are you sure you want to delete " + selectedVehicle.get().getName() + "?";

            ConfirmDialog confirmDeleteDialog = new ConfirmDialog(
                    "Delete Vehicle", message,
                    "Delete",
                    e -> onDeleteConfirm(selectedVehicle.get()),
                    "Cancel",
                    e -> e.getSource().close());

            confirmDeleteDialog.setConfirmButtonTheme("error primary");

            confirmDeleteDialog.open();
        }
    }

    /**
     * Fired when delete confirm dialog is confirmed by user. Deletes  Vehicle.
     *
     * @param vehicle The Vehicle to delete.
     */

    private void onDeleteConfirm(Vehicle vehicle) {
        if (vehicle != null) {
            vehicleService.delete(vehicle);
            VehicleUpdates("Deleted Vehicle " + vehicle.getName());
        }
    }


    /**
     * Fired when a new Vehicle is successfully registered using the Vehicle editor dialog.
     *
     * @param event the event that fired this method
     */

    private void onVehicleRegisteredSuccess(ComponentEvent<VehicleEditorDialog> event) {
        Vehicle newVehicle = event.getSource().getVehicle();
        vehicleEditorDialog.close();

        if (newVehicle != null) {
            VehicleUpdates("Successfully added new Vehicle " + newVehicle.getName());
        }
    }

    /**
     * Fired when an existing Vehicle is successfully edited using the Vehicle editor dialog.
     *
     * @param event the event that fired this method
     */

    private void onVehicleEditedSuccess(ComponentEvent<VehicleEditorDialog> event) {
        Vehicle editedVehicle = event.getSource().getVehicle();
        vehicleEditorDialog.close();

        if (editedVehicle != null) {
            VehicleUpdates("Successfully edited Vehicle " + editedVehicle.getName());
        }
    }

    /**
     * This method includes some common functionality when any change to Vehicles occurs
     *
     * @param successMessage the message text to display to the user in a notification
     */

    private void VehicleUpdates(String successMessage) {
        updateVehicleList();

        Notification notification = new Notification(
                successMessage,
                4000,
                Notification.Position.TOP_END
        );

        notification.open();
    }

    /**
     * Creates the view history icon button for each row in the grid
     *
     * @param vehicle the vehicle instance the icon button is associated with
     * @return the icon button to be returned
     */
    private Button viewHistory(Vehicle vehicle) {
        Button updateButton = new Button(VaadinIcon.CAR.create(), buttonClickEvent ->
                showVehicleHistoryDialog(vehicle));
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
