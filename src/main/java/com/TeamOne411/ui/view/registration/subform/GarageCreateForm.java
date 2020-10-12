package com.TeamOne411.ui.view.registration.subform;

import com.TeamOne411.backend.entity.Garage;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

public class GarageCreateForm extends VerticalLayout {
    private TextField companyName = new TextField("Company name");
    private TextField phoneNumber = new TextField("Phone number");
    private TextField address = new TextField("Address");
    private Button backButton = new Button("Back To My Information", new Icon(VaadinIcon.ARROW_LEFT));
    private Button nextButton = new Button("Confirm Details", new Icon(VaadinIcon.ARROW_RIGHT));
    private ShortcutRegistration enterKeyRegistration;

    Binder<Garage> binder = new BeanValidationBinder<>(Garage.class);
    private Garage garage = new Garage();

    public GarageCreateForm() {
        // initial view setup
        addClassName("garage-create-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        nextButton.setIconAfterText(true);
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        binder.bindInstanceFields(this);

        // set button click listeners
        backButton.addClickListener(e -> fireEvent(new BackEvent(this)));
        nextButton.addClickListener(e -> validateAndFireNext());

        phoneNumber.setValueChangeMode(ValueChangeMode.LAZY);
        phoneNumber.setPlaceholder("555-555-5555");

        add(
                new H3("Tell us about your garage"),
                new H5("You're almost done"),
                companyName,
                phoneNumber,
                address,
                new HorizontalLayout(backButton, nextButton)
        );
    }

    private void validateAndFireNext() {
        binder.validate();
        if (!binder.isValid()) return;
        fireEvent(new GarageCreateForm.NextEvent(this));
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

    public void setEnterShortcutRegistration(boolean addRegistration) {
        if (addRegistration) enterKeyRegistration = nextButton.addClickShortcut(Key.ENTER);
        else if (enterKeyRegistration != null) enterKeyRegistration.remove();
    }

    // Button event definitions begin
    public static class BackEvent extends ComponentEvent<GarageCreateForm> {
        BackEvent(GarageCreateForm source) {
            super(source, false);
        }
    }

    public static class NextEvent extends ComponentEvent<GarageCreateForm> {
        NextEvent(GarageCreateForm source) {
            super(source, false);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    // Button event definitions end
}
