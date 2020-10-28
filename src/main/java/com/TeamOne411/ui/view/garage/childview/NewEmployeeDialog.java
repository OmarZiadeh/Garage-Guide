package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.ui.view.registration.subform.GarageEmployeeRegisterForm;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class NewEmployeeDialog extends Dialog {
    private GarageEmployeeRegisterForm garageEmployeeRegisterForm;

    public NewEmployeeDialog(UserDetailsService userDetailsService) {
        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        garageEmployeeRegisterForm = new GarageEmployeeRegisterForm(userDetailsService);
        garageEmployeeRegisterForm.setBackButtonText("Cancel");
        garageEmployeeRegisterForm.setNextButtonText("Complete Registration");

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        container.add(new H3("Register New Employee"), garageEmployeeRegisterForm);

        add(container);
    }
}

