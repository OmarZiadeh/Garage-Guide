package com.TeamOne411.ui.view.registration;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RegisterForm extends VerticalLayout {
    public RegisterForm() {
        H3 userTypePrompt = new H3("I am a...");
        Button carOwnerSelectButton = new Button("Car Owner");
        Button garageAdminSelectButton = new Button("Garage Owner/Manager");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(
                userTypePrompt,
                carOwnerSelectButton,
                garageAdminSelectButton
        );
    }
}
