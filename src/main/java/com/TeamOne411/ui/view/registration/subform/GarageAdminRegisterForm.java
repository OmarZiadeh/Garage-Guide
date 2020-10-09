package com.TeamOne411.ui.view.registration.subform;

import com.TeamOne411.backend.entity.Garage;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

// todo extend this from an abstract "UserAccountRegisterForm" for reuse by CarOwner
public class GarageAdminRegisterForm extends VerticalLayout {
    private TextField userName = new TextField("Desired Username");
    // todo add password and confirm password fields
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField emailAddress = new TextField("Email Address");
    private Garage employer = new Garage();
    private boolean isAdmin = true;

    public GarageAdminRegisterForm() {
        // initial view setup
        addClassName("garage-admin-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(
                new H3("Let's start with your information."),
                new H5("We'll ask for details about your garage on the next page"),
                userName,
                firstName,
                lastName,
                emailAddress
        );
    }

    private boolean isFormValid() {
        // need to hook this up to a series of change listeners? look it up
        return false;
    }
}
