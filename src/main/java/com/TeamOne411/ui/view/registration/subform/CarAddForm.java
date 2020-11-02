package com.TeamOne411.ui.view.registration.subform;

import com.TeamOne411.backend.entity.Car;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Select;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

public class CarAddForm extends VerticalLayout {

    //these need to be lists but vehicle API needs work, replace SELECTS
    private Select carMake = new Select("Make");
    private Select carModel = new Select("Model");
    private Select carYear = new Select("Year);
    private TextField carColor = new TextField("Color");
    private TextField carVIN = new TextField("VIN");
    private Button backButton = new Button("Back To My Information", new Icon(VaadinIcon.ARROW_LEFT));
    private Button nextButton = new Button("Confirm Details", new Icon(VaadinIcon.ARROW_RIGHT));
    private ShortcutRegistration enterKeyRegistration;

    Binder<Car> binder = new BeanValidationBinder<>(Car.class);
    private Car car = new Car();

    public CarAddForm() {
        // initial view setup
        addClassName("car-add-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        nextButton.setIconAfterText(true);
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        binder.bindInstanceFields(this);

        // set button click listeners
        backButton.addClickListener(e -> fireEvent(new BackEvent(this)));
        nextButton.addClickListener(e -> validateAndFireNext());

        carVIN.setValueChangeMode(ValueChangeMode.LAZY);
        carVIN.setPlaceholder("1ABCD12ABCD123456");

        add(
                new H3("Tell us about your garage"),
                new H5("You're almost done"),
                //incorrect components causing error, temporary
                carMake,
                carModel,
                carYear,
                carColor,
                carVIN,
                new HorizontalLayout(backButton, nextButton)
        );
    }

    private void validateAndFireNext() {
        binder.validate();
        if (!binder.isValid()) return;
        fireEvent(new CarAddForm.NextEvent(this));
    }

    public Car getValidCar() {
        try {
            binder.writeBean(car);
            return car;
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        // return null if try block fails for any reason
        return null;
    }

    public void setEnterShortcutRegistration(boolean addRegistration) {
        if (addRegistration) enterKeyRegistration = nextButton.addClickShortcut(Key.ENTER);
        else if (enterKeyRegistration != null) enterKeyRegistration.remove();
    }


    // Button event definitions begin
    public static class BackEvent extends ComponentEvent<CarAddForm> {
        BackEvent(CarAddForm source) {
            super(source, false);
        }
    }

    public static class NextEvent extends ComponentEvent<CarAddForm> {
        NextEvent(CarAddForm source) {
            super(source, false);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    // Button event definitions end
}
