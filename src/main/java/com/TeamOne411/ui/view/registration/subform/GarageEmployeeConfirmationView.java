package com.TeamOne411.ui.view.registration.subform;

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
import com.vaadin.flow.shared.Registration;

public class GarageEmployeeConfirmationView extends VerticalLayout {
    private Button backButton = new Button("Back To Garage Details", new Icon(VaadinIcon.ARROW_LEFT));
    private Button nextButton = new Button("Complete Registration", new Icon(VaadinIcon.ARROW_RIGHT));
    private ShortcutRegistration enterKeyRegistration;

    public GarageEmployeeConfirmationView() {
        // initial view setup
        addClassName("garage-confirm-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        nextButton.setIconAfterText(true);
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // set button click listeners
        backButton.addClickListener(e -> fireEvent(new GarageEmployeeConfirmationView.BackEvent(this)));
        nextButton.addClickListener(e -> fireEvent(new GarageEmployeeConfirmationView.NextEvent(this)));

        add(
                new H3("All good?"),
                new H5("Please verify your details, go back if there are any mistakes."),
                new HorizontalLayout(backButton, nextButton)
        );
    }

    public void setEnterShortcutRegistration(boolean addRegistration) {
        if (addRegistration) enterKeyRegistration = nextButton.addClickShortcut(Key.ENTER);
        else if (enterKeyRegistration != null) enterKeyRegistration.remove();
    }

    // Button event definitions begin
    public static class BackEvent extends ComponentEvent<GarageEmployeeConfirmationView> {
        BackEvent(GarageEmployeeConfirmationView source) {
            super(source, false);
        }
    }

    public static class NextEvent extends ComponentEvent<GarageEmployeeConfirmationView> {
        NextEvent(GarageEmployeeConfirmationView source) {
            super(source, false);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    // Button event definitions end
}
