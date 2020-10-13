package com.TeamOne411.ui.view.registration.subform;

import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.UserDetailsService;
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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import javax.validation.constraints.NotEmpty;

// todo extend this from an abstract "UserAccountRegisterForm" for reuse by CarOwner
public class GarageAdminRegisterForm extends VerticalLayout {
    private TextField username = new TextField("Desired Username");
    private PasswordField password = new PasswordField("Password");
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email Address");
    private Button backButton = new Button("Back To User Selection", new Icon(VaadinIcon.ARROW_LEFT));
    private Button nextButton = new Button("Enter Garage Info", new Icon(VaadinIcon.ARROW_RIGHT));

    @NotEmpty(message = "Please enter your password again to confirm.")
    private PasswordField confirmPassword = new PasswordField("Confirm Password");

    private ShortcutRegistration enterKeyRegistration;
    private UserDetailsService userDetailsService;

    Binder<GarageEmployee> binder = new BeanValidationBinder<>(GarageEmployee.class);
    private GarageEmployee garageEmployee = new GarageEmployee();

    public GarageAdminRegisterForm(UserDetailsService userDetailsService) {

        this.userDetailsService = userDetailsService;

        // initial view setup
        addClassName("garage-admin-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        nextButton.setIconAfterText(true);
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        binder.bindInstanceFields(this);

        // set button click listeners
        backButton.addClickListener(e -> fireEvent(new BackEvent(this)));
        nextButton.addClickListener(e -> validateAndFireNext());

        // hook up username field to validator
        username.setValueChangeMode(ValueChangeMode.LAZY);
        username.addValueChangeListener(e -> validateUsername());

        // hook up email field to validator
        email.setValueChangeMode(ValueChangeMode.LAZY);
        email.addValueChangeListener(e -> validateEmailField());

        // hook up password fields to validator
        password.addValueChangeListener(e -> validatePasswordFields());
        confirmPassword.setValueChangeMode(ValueChangeMode.LAZY);
        confirmPassword.addValueChangeListener(e -> validatePasswordFields());

        firstName.setValueChangeMode(ValueChangeMode.LAZY);
        lastName.setValueChangeMode(ValueChangeMode.LAZY);

        add(
                new H3("Let's start with your information."),
                new H5("We'll ask for details about your garage on the next page"),
                username,
                password,
                confirmPassword,
                firstName,
                lastName,
                email,
                new HorizontalLayout(backButton, nextButton)
        );
    }

    private boolean validateUsername() {
        if (userDetailsService.isUsernameExisting(username.getValue())){
            username.setErrorMessage("Username already taken!");
            username.setInvalid(true);
            return false;
        }

        return !username.isInvalid();
    }

    private boolean validatePasswordFields() {
        // make sure the passwords match
        if (!password.getValue().equals(confirmPassword.getValue())) {
            // only set error message if confirm password field isn't empty
            if (!confirmPassword.isEmpty()) {
                confirmPassword.setErrorMessage("Passwords don't match!");
                confirmPassword.setInvalid(true);
            }
            return false;
        }

        confirmPassword.setInvalid(false);
        return !password.isInvalid();
    }

    private boolean validateEmailField() {
        // check to make sure the email doesn't already exist
        if (userDetailsService.isEmailExisting(email.getValue())){
            email.setErrorMessage("An account with this email address already exists!");
            email.setInvalid(true);
            return false;
        }

        return !email.isInvalid();
    }

    private void validateAndFireNext() {
        binder.validate();
        if (!validateUsername()) return;
        if (!validatePasswordFields()) return;
        if (!validateEmailField()) return;
        if (!binder.isValid()) return;
        fireEvent(new NextEvent(this));
    }

    public GarageEmployee getValidGarageEmployee() {
        try {
            binder.writeBean(garageEmployee);
            garageEmployee.setIsAdmin(true);
            garageEmployee.setIsEnabled(true);
            return garageEmployee;
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
    public static class BackEvent extends ComponentEvent<GarageAdminRegisterForm> {
        BackEvent(GarageAdminRegisterForm source) {
            super(source, false);
        }
    }

    public static class NextEvent extends ComponentEvent<GarageAdminRegisterForm> {
        NextEvent(GarageAdminRegisterForm source) {
            super(source, false);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    // Button event definitions end
}
