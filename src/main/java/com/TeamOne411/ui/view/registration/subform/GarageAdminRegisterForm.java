package com.TeamOne411.ui.view.registration.subform;

import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

// todo extend this from an abstract "UserAccountRegisterForm" for reuse by CarOwner
public class GarageAdminRegisterForm extends VerticalLayout {
    private TextField username = new TextField("Desired Username");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm Password");
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email Address");

    Binder<GarageEmployee> binder = new BeanValidationBinder<>(GarageEmployee.class);
    private GarageEmployee garageEmployee = new GarageEmployee();

    public GarageAdminRegisterForm() {
        // initial view setup
        addClassName("garage-admin-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        binder.bindInstanceFields(this);

        add(
                new H3("Let's start with your information."),
                new H5("We'll ask for details about your garage on the next page"),
                username,
                password,
                confirmPassword,
                firstName,
                lastName,
                email
        );
    }

    private boolean isFormValid() {
        // need to hook this up to a series of change listeners? look it up
        return false;
    }

    public GarageEmployee getValidGarageEmployee() {
        try {
            binder.writeBean(garageEmployee);
            garageEmployee.setIsAdmin(true);
            garageEmployee.setIsEnabled(true);
            return garageEmployee;
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        // return null if try block fails for any reason
        return null;
    }
}
