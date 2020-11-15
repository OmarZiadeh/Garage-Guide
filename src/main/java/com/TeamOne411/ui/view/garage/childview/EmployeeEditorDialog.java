package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.exceptions.EmailExistsException;
import com.TeamOne411.backend.service.UserDetailsService;
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
public class EmployeeEditorDialog extends Dialog {
    private GarageEmployeeRegisterForm garageEmployeeRegisterForm;
    private UserDetailsService userDetailsService;
    private Garage garage;
    private GarageEmployee employee;
    private boolean isEditMode = false;

    /**
     * This constructor is for the dialog to create a new employee.
     * @param userDetailsService
     * @param garage
     */
    public EmployeeEditorDialog(UserDetailsService userDetailsService, Garage garage) {
        this.userDetailsService = userDetailsService;
        this.garage = garage;

        initDialog("Register New Employee");
        garageEmployeeRegisterForm.setIsAdminToggleValue(false);
    }

    /**
     * This constructor is for the dialog to edit an existing employee record.
     * @param userDetailsService
     * @param employee
     */
    public EmployeeEditorDialog(UserDetailsService userDetailsService, GarageEmployee employee) {
        this.userDetailsService = userDetailsService;
        this.employee = employee;
        this.garage = employee.getGarage();
        isEditMode = true;

        initDialog("Edit Employee Details");
        garageEmployeeRegisterForm.prefillForm(employee);
    }

    /**
     * Initializes the dialog with some common functions. Should be called by all constructors.
     * @param title The title text to give to the dialog
     */
    private void initDialog(String title) {
        garageEmployeeRegisterForm = new GarageEmployeeRegisterForm(userDetailsService);
        garageEmployeeRegisterForm.setIsEditMode(isEditMode);
        garageEmployeeRegisterForm.addListener(GarageEmployeeRegisterForm.BackEvent.class, this::onCancelClick);
        garageEmployeeRegisterForm.addListener(GarageAdminRegisterForm.NextEvent.class, this::onComplete);
        garageEmployeeRegisterForm.setBackButtonText("Cancel");
        garageEmployeeRegisterForm.setNextButtonText(isEditMode ? "Save Changes" : "Complete Registration");
        garageEmployeeRegisterForm.setIsAdminToggleEnabled(true);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        container.add(new H3(title), garageEmployeeRegisterForm);
        add(container);
    }

    /**
     * Getter for the employee field.
     * @return
     */
    public GarageEmployee getEmployee() {
        return employee;
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
        employee = garageEmployeeRegisterForm.getValidGarageEmployee();

        // they should be valid if they aren't null
        if (employee != null && garage != null) {
            employee.setGarage(garage);

            if (!isEditMode) {
                try {
                    userDetailsService.registerNewUser(employee);
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
                userDetailsService.updateUser(employee);
                fireEvent(new EditEmployeeSuccessEvent(this));
            }

        } else {
            // todo display error
        }
    }

    /**
     * Event to emit when a new employee is successfully added.
     */
    public static class AddEmployeeSuccessEvent extends ComponentEvent<EmployeeEditorDialog> {
        AddEmployeeSuccessEvent(EmployeeEditorDialog source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when an existing employee is successfully edited.
     */
    public static class EditEmployeeSuccessEvent extends ComponentEvent<EmployeeEditorDialog> {
        EditEmployeeSuccessEvent(EmployeeEditorDialog source) {
            super(source, false);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

