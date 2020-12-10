package com.TeamOne411.ui.view.registration;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.*;
import com.TeamOne411.backend.service.exceptions.EmailExistsException;
import com.TeamOne411.backend.service.exceptions.PhoneNumberExistsException;
import com.TeamOne411.backend.service.exceptions.UsernameExistsException;
import com.TeamOne411.ui.view.registration.subform.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "register")
public class RegisterView extends VerticalLayout {
    private GarageAdminRegisterForm garageAdminRegisterForm;
    private CarOwnerRegisterForm carOwnerRegisterForm;
    private CarOwnerConfirmationView carOwnerConfirmView;
    private GarageCreateForm garageCreateForm = new GarageCreateForm();
    private GarageEmployeeConfirmationView garageConfirmView;
    private H3 userTypePrompt = new H3("I am a...");
    private Button carOwnerSelectButton = new Button("Car Owner");
    private Button garageAdminSelectButton = new Button("Garage Owner/Manager");
    private RegistrationState state = RegistrationState.USER_TYPE_SELECTION;
    private RegistrationPath path;
    private GarageService garageService;
    private UserDetailsService userDetailsService;
    private ServiceCatalogService serviceCatalogService;
    private BusinessHoursService businessHoursService;

    public RegisterView(
            GarageService garageService,
            UserDetailsService userDetailsService,
            ServiceCatalogService serviceCatalogService,
            BusinessHoursService businessHoursService
    ) {
        // field assignments
        this.garageService = garageService;
        this.userDetailsService = userDetailsService;
        this.garageAdminRegisterForm = new GarageAdminRegisterForm(userDetailsService);
        this.garageConfirmView = new GarageEmployeeConfirmationView();

        this.carOwnerRegisterForm = new CarOwnerRegisterForm(userDetailsService);
        this.carOwnerConfirmView = new CarOwnerConfirmationView();

        this.serviceCatalogService = serviceCatalogService;
        this.businessHoursService = businessHoursService;

        // initial view setup
        addClassName("register-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setComponentAttributesForState();

        // coming soon
        carOwnerSelectButton.addClickListener(e -> {
            state = RegistrationState.CAR_OWNER_INFO;
            setComponentAttributesForState();
            path = RegistrationPath.CAR_OWNER;
        });
        carOwnerRegisterForm.addListener(CarOwnerRegisterForm.BackEvent.class, this::backClick);
        carOwnerRegisterForm.addListener(CarOwnerRegisterForm.NextEvent.class, this::nextClick);
        carOwnerConfirmView.addListener(CarOwnerConfirmationView.BackEvent.class, this::backClick);
        carOwnerConfirmView.addListener(CarOwnerConfirmationView.NextEvent.class, this::onComplete);

        // add listener to the select button for garage admin
        // todo can we animate the sub forms as they come in and out?
        garageAdminSelectButton.addClickListener(e -> {
            state = RegistrationState.GARAGE_ADMIN_INFO;
            setComponentAttributesForState();
            path = RegistrationPath.GARAGE_ADMIN;
        });

        // connect the click handlers to the form events
        garageAdminRegisterForm.addListener(GarageAdminRegisterForm.BackEvent.class, this::backClick);
        garageAdminRegisterForm.addListener(GarageAdminRegisterForm.NextEvent.class, this::nextClick);
        garageCreateForm.addListener(GarageCreateForm.BackEvent.class, this::backClick);
        garageCreateForm.addListener(GarageCreateForm.NextEvent.class, this::nextClick);
        garageConfirmView.addListener(GarageEmployeeConfirmationView.BackEvent.class, this::backClick);
        garageConfirmView.addListener(GarageEmployeeConfirmationView.NextEvent.class, this::onComplete);

        // todo make next button enabled only when current form is valid

        // build view
        add(
                new H1("Garage Guide - New User Registration"),
                userTypePrompt,
                carOwnerSelectButton,
                carOwnerRegisterForm,
                carOwnerConfirmView,
                garageAdminSelectButton,
                garageAdminRegisterForm,
                garageCreateForm,
                garageConfirmView
        );
    }

    private RegistrationState getNextState(boolean reverse) {
        if (state == null) return null;

        switch (state) {
            case CAR_OWNER_INFO:
                return reverse ? RegistrationState.USER_TYPE_SELECTION : RegistrationState.CAR_OWNER_CONFIRMATION;
            case GARAGE_ADMIN_INFO:
                return reverse ? RegistrationState.USER_TYPE_SELECTION : RegistrationState.GARAGE_INFO;
            case GARAGE_INFO:
                return reverse ? RegistrationState.GARAGE_ADMIN_INFO : RegistrationState.GARAGE_CONFIRMATION;
            case GARAGE_CONFIRMATION:
                return reverse ? RegistrationState.GARAGE_INFO : null;
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

        // todo migrate these into methods in each form (example below):
        // garageCreateForm.setActive(state == RegistrationState.GARAGE_INFO)
        // also in these methods: set focus to first text field on setActive(true)

        // form and subview visibilities
        carOwnerRegisterForm.setVisible(state == RegistrationState.CAR_OWNER_INFO);
        carOwnerConfirmView.setVisible(state == RegistrationState.CAR_OWNER_CONFIRMATION);
        garageAdminRegisterForm.setVisible(state == RegistrationState.GARAGE_ADMIN_INFO);
        garageCreateForm.setVisible(state == RegistrationState.GARAGE_INFO);
        garageConfirmView.setVisible(state == RegistrationState.GARAGE_CONFIRMATION);

        // enter key registrations
        garageAdminRegisterForm.setEnterShortcutRegistration(state == RegistrationState.GARAGE_ADMIN_INFO);
        garageCreateForm.setEnterShortcutRegistration(state == RegistrationState.GARAGE_INFO);
        garageConfirmView.setEnterShortcutRegistration(state == RegistrationState.GARAGE_CONFIRMATION);

        if (state == RegistrationState.GARAGE_CONFIRMATION) {
            garageConfirmView.setEntitiesForConfirmation(garageAdminRegisterForm.getValidGarageEmployee(), garageCreateForm.getValidGarage());
        } else if (state == RegistrationState.CAR_OWNER_CONFIRMATION) {
            carOwnerConfirmView.setEntitiesForConfirmation(carOwnerRegisterForm.getValidCarOwner());
        }
    }

    private void backClick(ComponentEvent event) {
        state = getNextState(true);
        setComponentAttributesForState();
    }

    private void nextClick(ComponentEvent event) {
        state = getNextState(false);
        setComponentAttributesForState();
    }

    private void onComplete(ComponentEvent event) {
        if (path == RegistrationPath.CAR_OWNER) {
            // todo once car owner form is done:
            // get carOwner
            CarOwner carOwner = carOwnerRegisterForm.getValidCarOwner();
            if (carOwner != null) {
                try {
                    userDetailsService.registerNewUser(carOwner);
                    getUI().ifPresent(ui -> ui.navigate("login"));
                } catch (EmailExistsException emailEx) {
                    // todo display error
                } catch (UsernameExistsException usernameEx) {
                    // todo display error
                } catch (PhoneNumberExistsException phoneNumberEx) {
                    // todo display error
                }
            } else {
                // todo display error
            }

        } else if (path == RegistrationPath.GARAGE_ADMIN) {
            // get the employee and the garage from their forms
            GarageEmployee garageEmployee = garageAdminRegisterForm.getValidGarageEmployee();
            Garage garage = garageCreateForm.getValidGarage();

            // they should be valid if they aren't null
            if (garageEmployee != null && garage != null) {
                // save the garage first because the employee, services and business hours have dependencies
                garageService.save(garage);
                // create the default services for the garage
                serviceCatalogService.initializeDefaultServices(garage);
                // create the default business hours for the garage
                businessHoursService.initializeBusinessHours(garage);

                garageEmployee.setGarage(garage);

                try {
                    userDetailsService.registerNewUser(garageEmployee);
                    getUI().ifPresent(ui -> ui.navigate("login"));
                } catch (EmailExistsException emailEx) {
                    // todo display error
                    // todo delete garage just created
                } catch (UsernameExistsException usernameEx) {
                    // todo display error
                    // todo delete garage just created
                    // redundant but necessary catch clause
                } catch (PhoneNumberExistsException e) {
                    e.printStackTrace();
                }
            } else {
                // todo display error
            }
        }
    }

    private enum RegistrationState {
        USER_TYPE_SELECTION,
        CAR_OWNER_INFO,
        CAR_OWNER_CONFIRMATION,
        GARAGE_ADMIN_INFO,
        GARAGE_INFO,
        GARAGE_CONFIRMATION,
    }

    private enum RegistrationPath {
        CAR_OWNER,
        GARAGE_ADMIN
    }
}
