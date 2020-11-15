package com.TeamOne411.ui.view.registration.subform;


import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

public class CarOwnerConfirmationView extends VerticalLayout {
    private Button backButton = new Button("Back To Account Details", new Icon(VaadinIcon.ARROW_LEFT));
    private Button nextButton = new Button("Complete Registration", new Icon(VaadinIcon.ARROW_RIGHT));
    private Text username = new Text("");
    private Text name = new Text("");
    private Text email = new Text("");
    private Text phoneNumber = new Text("");
    private Text address = new Text("");
    private ShortcutRegistration enterKeyRegistration;

    public CarOwnerConfirmationView() {
        // initial view setup
        addClassName("car-owner-confirm-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        nextButton.setIconAfterText(true);
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // set button click listeners
        backButton.addClickListener(e -> fireEvent(new CarOwnerConfirmationView.BackEvent(this)));
        nextButton.addClickListener(e -> fireEvent(new CarOwnerConfirmationView.NextEvent(this)));



        add(
                new H3("All good?"),
                new H5("Please verify your details, go back if there are any mistakes."),
                new HorizontalLayout(new Text("Username: "), username),
                new HorizontalLayout(new Text("Your name: "), name),
                new HorizontalLayout(new Text("Your phone: "), phoneNumber),
                new HorizontalLayout(new Text("Your address: "), address),
                new HorizontalLayout(new Text("Your email: "), email),
                new HorizontalLayout(backButton, nextButton)
        );
    }

    public void setEntitiesForConfirmation(CarOwner carOwner) {
        if (carOwner == null) return;
        username.setText(carOwner.getUsername());
        name.setText(carOwner.getFirstName() + " " + carOwner.getLastName());
        email.setText(carOwner.getEmail());
        phoneNumber.setText(carOwner.getPhoneNumber());
        address.setText(carOwner.getAddress());
    }

    public void setEnterShortcutRegistration(boolean addRegistration) {
        if (addRegistration) enterKeyRegistration = nextButton.addClickShortcut(Key.ENTER);
        else if (enterKeyRegistration != null) enterKeyRegistration.remove();
    }

    // Button event definitions begin
    public static class BackEvent extends ComponentEvent<CarOwnerConfirmationView> {
        BackEvent(CarOwnerConfirmationView source) {
            super(source, false);
        }
    }

    public static class NextEvent extends ComponentEvent<CarOwnerConfirmationView> {
        NextEvent(CarOwnerConfirmationView source) {
            super(source, false);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    // Button event definitions end
}

