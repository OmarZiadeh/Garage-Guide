package com.TeamOne411.ui.view.registration.subform;

import com.TeamOne411.backend.entity.Garage;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class GarageCreateForm extends VerticalLayout {
    private TextField companyName = new TextField("Company name");
    private TextField phoneNumber = new TextField("Phone number");
    private TextField address = new TextField("Address");

    Binder<Garage> binder = new BeanValidationBinder<>(Garage.class);
    private Garage garage = new Garage();

    public GarageCreateForm() {
        // initial view setup
        addClassName("garage-create-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        binder.bindInstanceFields(this);

        add(
                new H3("Tell us about your garage"),
                new H5("You're almost done"),
                companyName,
                phoneNumber,
                address
        );
    }

    public Garage getValidGarage() {
        try {
            binder.writeBean(garage);
            return garage;
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        // return null if try block fails for any reason
        return null;
    }
}
