package com.TeamOne411.ui.view.registration.subform;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class GarageCreateForm extends VerticalLayout {
    private TextField companyName = new TextField("Company name");
    private TextField phoneNumber = new TextField("Phone number");
    private TextField address = new TextField("Address");

    public GarageCreateForm() {
        // initial view setup
        addClassName("garage-create-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(
                new H3("Tell us about your garage"),
                new H5("You're almost done"),
                companyName,
                phoneNumber,
                address
        );
    }
}
