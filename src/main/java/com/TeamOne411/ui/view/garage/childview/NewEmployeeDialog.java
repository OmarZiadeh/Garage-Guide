package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.EmailExistsException;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.backend.service.UsernameExistsException;
import com.TeamOne411.ui.view.registration.subform.GarageAdminRegisterForm;
import com.TeamOne411.ui.view.registration.subform.GarageEmployeeRegisterForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class NewEmployeeDialog extends Dialog {
    private GarageEmployeeRegisterForm garageEmployeeRegisterForm;
    private UserDetailsService userDetailsService;
    private Garage garage;
    private GarageEmployee newEmployee;

    public NewEmployeeDialog(UserDetailsService userDetailsService, Garage garage) {
        this.userDetailsService = userDetailsService;
        this.garage = garage;
        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        garageEmployeeRegisterForm = new GarageEmployeeRegisterForm(userDetailsService);
        garageEmployeeRegisterForm.setBackButtonText("Cancel");
        garageEmployeeRegisterForm.setNextButtonText("Complete Registration");
        garageEmployeeRegisterForm.addListener(GarageEmployeeRegisterForm.BackEvent.class, this::onCancelClick);
        garageEmployeeRegisterForm.addListener(GarageAdminRegisterForm.NextEvent.class, this::onComplete);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        container.add(new H3("Register New Employee"), garageEmployeeRegisterForm);

        add(container);
    }

    private void onCancelClick(ComponentEvent event) {
        close();
    }

    private void onComplete(ComponentEvent event) {
        // get the employee
        newEmployee = garageEmployeeRegisterForm.getValidGarageEmployee();

        // they should be valid if they aren't null
        if (newEmployee != null && garage != null) {
            newEmployee.setGarage(garage);

            try {
                userDetailsService.registerNewUser(newEmployee);
                fireEvent(new SuccessEvent(this));
            } catch (EmailExistsException emailEx) {
                // todo display error
                // todo delete garage just created
            } catch (UsernameExistsException usernameEx) {
                // todo display error
                // todo delete garage just created
            }
        } else {
            // todo display error
        }
    }

    public static class SuccessEvent extends ComponentEvent<NewEmployeeDialog> {
        SuccessEvent(NewEmployeeDialog source) {
            super(source, false);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public GarageEmployee getNewEmployee() {
        return newEmployee;
    }
}

