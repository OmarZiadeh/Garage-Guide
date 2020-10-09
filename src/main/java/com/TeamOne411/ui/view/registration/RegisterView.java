package com.TeamOne411.ui.view.registration;

import com.TeamOne411.ui.view.registration.subform.GarageAdminRegisterForm;
import com.TeamOne411.ui.view.registration.subform.GarageCreateForm;
import com.TeamOne411.ui.view.registration.subform.GarageEmployeeConfirmationView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "register")
public class RegisterView extends VerticalLayout {
    private GarageAdminRegisterForm garageAdminRegisterForm = new GarageAdminRegisterForm();
    private GarageCreateForm garageCreateForm = new GarageCreateForm();
    private GarageEmployeeConfirmationView garageConfirmView = new GarageEmployeeConfirmationView();
    private H3 userTypePrompt = new H3("I am a...");
    private Button carOwnerSelectButton = new Button("Car Owner (coming soon)");
    private Button garageAdminSelectButton = new Button("Garage Owner/Manager");
    private Button backButton = new Button("Back", new Icon(VaadinIcon.ARROW_LEFT));
    private Button nextButton = new Button("Next", new Icon(VaadinIcon.ARROW_RIGHT));
    private RegistrationState state = RegistrationState.USER_TYPE_SELECTION;

    public RegisterView() {
        // initial view setup
        addClassName("register-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        nextButton.setIconAfterText(true);

        // coming soon
        carOwnerSelectButton.setEnabled(false);

        // add listener to the select button for garage admin
        // todo can we animate the sub forms as they come in and out?
        garageAdminSelectButton.addClickListener(e -> {
            state = RegistrationState.GARAGE_ADMIN_INFO;
            setVisibilitiesForCurrentState();
            setBackNextButtonTextFromState();
        });

        setVisibilitiesForCurrentState();

        backButton.addClickListener(e -> {
           state = getNextState(true);
           setVisibilitiesForCurrentState();
           setBackNextButtonTextFromState();
        });

        nextButton.addClickListener(e -> {
           state = getNextState(false);
           setVisibilitiesForCurrentState();
           setBackNextButtonTextFromState();
        });

        // todo make next button enabled only when current form is valid

        // build view
        add(
                new H1("Garage Guide - New User Registration"),
                userTypePrompt,
                carOwnerSelectButton,
                garageAdminSelectButton,
                garageAdminRegisterForm,
                garageCreateForm,
                garageConfirmView,
                new HorizontalLayout(backButton, nextButton)
        );
    }

    private void setInitialVisibility(boolean isInitial) {
        userTypePrompt.setVisible(isInitial);
        carOwnerSelectButton.setVisible(isInitial);
        garageAdminSelectButton.setVisible(isInitial);
        backButton.setVisible(!isInitial);
        nextButton.setVisible(!isInitial);
    }

    private void setVisibilitiesForCurrentState() {
        setInitialVisibility(state == RegistrationState.USER_TYPE_SELECTION);
        garageAdminRegisterForm.setVisible(state == RegistrationState.GARAGE_ADMIN_INFO);
        garageCreateForm.setVisible(state == RegistrationState.GARAGE_INFO);
        garageConfirmView.setVisible(state == RegistrationState.GARAGE_CONFIRMATION);
    }

    private RegistrationState getNextState(boolean reverse) {
        if (state == null) return null;

        switch (state) {
            case CAR_OWNER_INFO:
                return reverse ? RegistrationState.USER_TYPE_SELECTION : null;
            case GARAGE_ADMIN_INFO:
                return reverse ? RegistrationState.USER_TYPE_SELECTION : RegistrationState.GARAGE_INFO;
            case GARAGE_INFO:
                return reverse ? RegistrationState.GARAGE_ADMIN_INFO : RegistrationState.GARAGE_CONFIRMATION;
            case GARAGE_CONFIRMATION:
                return reverse ? RegistrationState.GARAGE_ADMIN_INFO : null;
            default:
                return null;
        }
    }

    private void setBackNextButtonTextFromState() {
        if (state == null) return;

        switch (state) {
            case CAR_OWNER_INFO:
                backButton.setText("Back");
                nextButton.setText("Complete Registration");
                break;
            case GARAGE_ADMIN_INFO:
                backButton.setText("Back");
                nextButton.setText("Enter Garage Info");
                break;
            case GARAGE_INFO:
                backButton.setText("Edit My Info");
                nextButton.setText("Confirm Details");
                break;
            case GARAGE_CONFIRMATION:
                backButton.setText("Edit Garage Info");
                nextButton.setText("Complete Registration");
                break;
        }
    }

    private enum RegistrationState {
        USER_TYPE_SELECTION,
        CAR_OWNER_INFO,
        GARAGE_ADMIN_INFO,
        GARAGE_INFO,
        GARAGE_CONFIRMATION,
    }
}
