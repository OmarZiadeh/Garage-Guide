package com.TeamOne411.ui.view.carowner.form;

import com.TeamOne411.backend.entity.Vehicle;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.service.VehicleService;
import com.TeamOne411.backend.service.api.car.ApiVehicleService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

/**
 * This class is a dialog that wraps GarageEmployeeRegisterForm.
 */
public class VehicleEditorDialog extends Dialog {
    private VehicleAddForm vehicleAddForm;
    private VehicleService vehicleService;
    private ApiVehicleService apiVehicleService;
    private Vehicle vehicle;
    private CarOwner carOwner;
    private boolean isEditMode = false;

    /**
     * This constructor is for the dialog to create a new vehicle
     * @param carOwner
     */
    public VehicleEditorDialog(CarOwner carOwner, ApiVehicleService apiVehicleService, VehicleService vehicleService) {
        this.apiVehicleService = apiVehicleService;
        this.carOwner = carOwner;
        this.vehicleService = vehicleService;

        initDialog("Register New Vehicle");
    }

    /**
     * This constructor is for the dialog to edit an existing vehicle record
     * @param vehicle
     */
    public VehicleEditorDialog(Vehicle vehicle, ApiVehicleService apiVehicleService, VehicleService vehicleService) {
        this.apiVehicleService = apiVehicleService;
        this.vehicle = vehicle;
        this.carOwner = vehicle.getCarOwner();
        this.vehicleService = vehicleService;
        isEditMode = true;

        initDialog("Edit Vehicle Details");
        vehicleAddForm.prefillForm(vehicle);
    }

    /**
     * Initializes the dialog with some common functions. Should be called by all constructors.
     * @param title The title text to give to the dialog
     */
    private void initDialog(String title) {
        vehicleAddForm = new VehicleAddForm(apiVehicleService);
        vehicleAddForm.setIsEditMode(isEditMode);
        vehicleAddForm.addListener(VehicleAddForm.BackEvent.class, this::onCancelClick);
        vehicleAddForm.addListener(VehicleAddForm.NextEvent.class, this::onComplete);
        vehicleAddForm.setBackButtonText("Cancel");
        vehicleAddForm.setNextButtonText(isEditMode ? "Save Changes" : "Add Vehicle");

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        container.add(new H3(title), vehicleAddForm);
        add(container);
    }

    /**
     * Getter for the employee field.
     * @return
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Fired when cancel button in child form is clicked. Closes the dialog.
     * @param event
     */
    private void onCancelClick(ComponentEvent event) {
        close();
    }

    /**
     * Fired when next button in child form is clicked. Propagates another event depending on Add or Edit mode.
     * @param event
     */
    private void onComplete(ComponentEvent event) {
        // get the employee
        vehicle = vehicleAddForm.getValidCar();

        // they should be valid if they aren't null
        if (vehicle != null) {
            vehicle.setCarOwner(carOwner);

            if (!isEditMode) {

                //possible try catch clause here to catch duplicate VIN
                vehicleService.save(vehicle);
                fireEvent(new AddVehicleSuccessEvent(this));

            } else {
                vehicleService.save(vehicle);
                fireEvent(new EditVehicleSuccessEvent(this));
            }

        } else {
            // todo display error
        }
    }

    /**
     * Event to emit when a new employee is successfully added.
     */
    public static class AddVehicleSuccessEvent extends ComponentEvent<VehicleEditorDialog> {
        AddVehicleSuccessEvent(VehicleEditorDialog source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when an existing employee is successfully edited.
     */
    public static class EditVehicleSuccessEvent extends ComponentEvent<VehicleEditorDialog> {
        EditVehicleSuccessEvent(VehicleEditorDialog source) {
            super(source, false);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

