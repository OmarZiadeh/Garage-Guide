package com.TeamOne411.ui.view.carowner.childview;

import com.TeamOne411.backend.entity.Vehicle;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.backend.service.VehicleService;
import com.TeamOne411.backend.service.exceptions.EmailExistsException;
import com.TeamOne411.backend.service.exceptions.PhoneNumberExistsException;
import com.TeamOne411.backend.service.exceptions.UsernameExistsException;
import com.TeamOne411.ui.view.registration.subform.GarageAdminRegisterForm;
import com.TeamOne411.ui.view.registration.subform.GarageEmployeeRegisterForm;
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
    private UserDetailsService userDetailsService;
    private VehicleService vehicleService;
    private Vehicle vehicle;
    private CarOwner carOwner;
    private boolean isEditMode = false;

    /**
     * This constructor is for the dialog to create a new vehicle
     * @param vehicle
     */
    public void VehicleAddForm(Vehicle vehicle) {
        this.vehicle = vehicle;

        initDialog("Register New Vehicle");
    }

    /**
     * This constructor is for the dialog to edit an existing vehicle record
     * @param vehicle
     */
    public VehicleEditorDialog(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.carOwner = vehicle.getCarOwner();
        isEditMode = true;

        initDialog("Edit Vehicle Details");
        vehicleAddForm.prefillForm(vehicle);
    }

    /**
     * Initializes the dialog with some common functions. Should be called by all constructors.
     * @param title The title text to give to the dialog
     */
    private void initDialog(String title) {
        vehicleAddForm = new VehicleAddForm(vehicleService);
        vehicleAddForm.setIsEditMode(isEditMode);
        vehicleAddForm.addListener(GarageEmployeeRegisterForm.BackEvent.class, this::onCancelClick);
        vehicleAddForm.addListener(GarageAdminRegisterForm.NextEvent.class, this::onComplete);
        vehicleAddForm.setBackButtonText("Cancel");
        vehicleAddForm.setNextButtonText(isEditMode ? "Save Changes" : "Complete Registration");
        vehicleAddForm.setIsAdminToggleEnabled(true);

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
                try {
                    vehicleService.registerNewVehicle(vehicle);
                    fireEvent(new AddEmployeeSuccessEvent(this));
                } catch (EmailExistsException emailEx) {
                    // todo display error
                    // todo delete garage just created
                } catch (UsernameExistsException usernameEx) {
                    // todo display error
                    // todo delete garage just created
                // redundant but necessary catch clause
                } catch (PhoneNumberExistsException e) {
                    e.printStackTrace();
                }
            } else {
                vehicleService.updateVehicle(vehicle);
                fireEvent(new EditEmployeeSuccessEvent(this));
            }

        } else {
            // todo display error
        }
    }

    /**
     * Event to emit when a new employee is successfully added.
     */
    public static class AddEmployeeSuccessEvent extends ComponentEvent<VehicleEditorDialog> {
        AddEmployeeSuccessEvent(VehicleEditorDialog source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when an existing employee is successfully edited.
     */
    public static class EditEmployeeSuccessEvent extends ComponentEvent<VehicleEditorDialog> {
        EditEmployeeSuccessEvent(VehicleEditorDialog source) {
            super(source, false);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

