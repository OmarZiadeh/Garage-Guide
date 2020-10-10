package com.TeamOne411.ui.view.registration;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.CarOwnerService;
import com.TeamOne411.backend.service.GarageEmployeeService;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.ui.view.registration.subform.GarageAdminRegisterForm;
import com.TeamOne411.ui.view.registration.subform.GarageCreateForm;
import com.TeamOne411.ui.view.registration.subform.GarageEmployeeConfirmationView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
    private Button completeButton = new Button("Complete Registration");
    private RegistrationState state = RegistrationState.USER_TYPE_SELECTION;
    private RegistrationPath path;
    private CarOwnerService carOwnerService;
    private GarageEmployeeService garageEmployeeService;
    private GarageService garageService;
    private UserDetailsService userDetailsService;

    public RegisterView(
            CarOwnerService carOwnerService,
            GarageEmployeeService garageEmployeeService,
            GarageService garageService,
            UserDetailsService userDetailsService
    ) {
        // field assignments
        this.carOwnerService = carOwnerService;
        this.garageEmployeeService = garageEmployeeService;
        this.garageService = garageService;
        this.userDetailsService = userDetailsService;

        // initial view setup
        addClassName("register-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        nextButton.setIconAfterText(true);
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        completeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // coming soon
        carOwnerSelectButton.setEnabled(false);

        // add listener to the select button for garage admin
        // todo can we animate the sub forms as they come in and out?
        garageAdminSelectButton.addClickListener(e -> {
            state = RegistrationState.GARAGE_ADMIN_INFO;
            setComponentAttributesForState();
            path = RegistrationPath.GARAGE_ADMIN;
        });

        backButton.addClickListener(e -> {
            state = getNextState(true);
            setComponentAttributesForState();
        });

        nextButton.addClickListener(e -> {
            state = getNextState(false);
            setComponentAttributesForState();
        });

        completeButton.addClickListener(e -> {
            onComplete();
        });

        setComponentAttributesForState();

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
                new HorizontalLayout(backButton, nextButton, completeButton)
        );
    }

    private void onComplete() {
        if (path == RegistrationPath.CAR_OWNER) {
            // todo once car owner form is done
            // get carOwner
            // pass to carOwnerService for saving
            // redirect to car owner's home screen
        } else if (path == RegistrationPath.GARAGE_ADMIN) {
            // get the employee and the garage from their forms
            GarageEmployee garageEmployee = garageAdminRegisterForm.getValidGarageEmployee();
            Garage garage = garageCreateForm.getValidGarage();

            // they should be valid if they aren't null
            if (garageEmployee != null && garage != null) {
                // save the garage first because the employee has dependency
                garageService.save(garage);

                garageEmployee.setGarage(garage);
                garageEmployeeService.save(garageEmployee);
                // todo if employee save fails, delete garage
            } else {
                // todo display error
            }
        }
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

    private void setComponentAttributesForState() {
        if (state == null) return;

        // initial screen visibility
        userTypePrompt.setVisible(state == RegistrationState.USER_TYPE_SELECTION);
        carOwnerSelectButton.setVisible(state == RegistrationState.USER_TYPE_SELECTION);
        garageAdminSelectButton.setVisible(state == RegistrationState.USER_TYPE_SELECTION);

        // form and subview visibilities
        garageAdminRegisterForm.setVisible(state == RegistrationState.GARAGE_ADMIN_INFO);
        garageCreateForm.setVisible(state == RegistrationState.GARAGE_INFO);
        garageConfirmView.setVisible(state == RegistrationState.GARAGE_CONFIRMATION);

        // button visibilities
        backButton.setVisible(state != RegistrationState.USER_TYPE_SELECTION);
        nextButton.setVisible(state != RegistrationState.USER_TYPE_SELECTION && state != RegistrationState.GARAGE_CONFIRMATION);
        completeButton.setVisible(state == RegistrationState.GARAGE_CONFIRMATION);
    }

    private enum RegistrationState {
        USER_TYPE_SELECTION,
        CAR_OWNER_INFO,
        GARAGE_ADMIN_INFO,
        GARAGE_INFO,
        GARAGE_CONFIRMATION,
    }

    private enum RegistrationPath {
        CAR_OWNER,
        GARAGE_ADMIN
    }
}
