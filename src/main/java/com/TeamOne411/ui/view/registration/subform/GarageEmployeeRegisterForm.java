package com.TeamOne411.ui.view.registration.subform;

import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.UserDetailsService;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

/**
 * This class is a VerticalLayout as a register form for GarageEmployees
 */
public class GarageEmployeeRegisterForm extends VerticalLayout {
    private TextField username = new TextField("Desired Username");
    private PasswordField password = new PasswordField("Password");
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email Address");
    private Button backButton = new Button("Back", new Icon(VaadinIcon.ARROW_LEFT));
    private Button nextButton = new Button("Next", new Icon(VaadinIcon.ARROW_RIGHT));
    private ToggleButton isAdmin = new ToggleButton("Garage Admin");

    private boolean isEditMode = false;

    @NotEmpty(message = "Please enter your password again to confirm.")
    private PasswordField confirmPassword = new PasswordField("Confirm Password");

    private ShortcutRegistration enterKeyRegistration;
    private UserDetailsService userDetailsService;

    Binder<GarageEmployee> binder = new BeanValidationBinder<>(GarageEmployee.class);
    private GarageEmployee garageEmployee = new GarageEmployee();

    public GarageEmployeeRegisterForm(UserDetailsService userDetailsService) {

        this.userDetailsService = userDetailsService;

        // initial view setup
        addClassName("garage-empl-subform");
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

        // disable isAdmin by default, parent component can enable selectively
        isAdmin.setEnabled(false);

        add(
                username,
                password,
                confirmPassword,
                firstName,
                lastName,
                email,
                isAdmin,
                new HorizontalLayout(backButton, nextButton)
        );
    }

    /**
     * Setter for the text of the back button
     * @param text text to set for the button
     */
    public void setBackButtonText(String text) {
        backButton.setText(text);
    }

    /**
     * Setter for the text of the next button.
     * @param text text to set for the button
     */
    public void setNextButtonText(String text) {
        nextButton.setText(text);
    }

    /**
     * Checks the username for uniqueness. If invalid, sets the error message for the control.
     * @return true for valid username, false if invalid
     */
    private boolean validateUsername() {
        // we need a way to ignore employee's username in edit mode
        boolean isEmployeeUsername = false;
        if (isEditMode && garageEmployee != null)
            isEmployeeUsername = garageEmployee.getUsername().equals(username.getValue());

        if (userDetailsService.isUsernameExisting(username.getValue()) && !isEmployeeUsername) {
            username.setErrorMessage("Username already taken!");
            username.setInvalid(true);
            return false;
        }

        return !username.isInvalid();
    }

    /**
     * Checks the two password fields for equality. If invalid, sets the error message for the control.
     * @return true for valid username, false if invalid
     */
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

    /**
     * Checks the email for uniqueness. If invalid, sets the error message for the control.
     * @return true for valid email, false if invalid
     */
    private boolean validateEmailField() {
        // we need a way to ignore employee's email in edit mode
        boolean isEmployeeEmail = false;
        if (isEditMode && garageEmployee != null)
            isEmployeeEmail = garageEmployee.getEmail().equals(email.getValue());

        // check to make sure the email doesn't already exist
        if (userDetailsService.isEmailExisting(email.getValue()) && !isEmployeeEmail) {
            email.setErrorMessage("An account with this email address already exists!");
            email.setInvalid(true);
            return false;
        }

        return !email.isInvalid();
    }

    /**
     * Called by next button click listener. Validates the form and fires the NextEvent.
     */
    private void validateAndFireNext() {
        if (!isEditMode) {
            if (!validateUsername()) return;
            if (!validatePasswordFields()) return;
        }

        if (!validateEmailField()) return;

        binder.validate();
        if (!binder.isValid()) return;

        fireEvent(new NextEvent(this));
    }

    /**
     * Attempts to return a validated employee instance from the values of the form controls.
     * @return a valid employee instance, or null if the employee instance is invalid
     */
    public GarageEmployee getValidGarageEmployee() {
        if (!isEditMode) {
            try {
                binder.writeBean(garageEmployee);
                garageEmployee.setIsEnabled(true);
                return garageEmployee;
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        } else {
            garageEmployee.setFirstName(firstName.getValue());
            garageEmployee.setLastName(lastName.getValue());
            garageEmployee.setEmail(email.getValue());
            garageEmployee.setIsAdmin(isAdmin.getValue());
            return garageEmployee;
        }

        // return null if try block fails for any reason
        return null;
    }

    /**
     * Sets/unsets the enter key shortcut registration
     * @param addRegistration true to set the registration, false to unset
     */
    public void setEnterShortcutRegistration(boolean addRegistration) {
        if (addRegistration) enterKeyRegistration = nextButton.addClickShortcut(Key.ENTER);
        else if (enterKeyRegistration != null) enterKeyRegistration.remove();
    }

    /**
     * Fills all form controls with known details of an existing employee.
     * @param employee the employee to fill details for
     */
    public void prefillForm(GarageEmployee employee) {
        garageEmployee = employee;
        binder.readBean(employee);

        if (employee != null) {
            username.setEnabled(false);

            // password fields get garbage
            password.setValue("xxxxxxxxxx");
            password.setEnabled(false);
            confirmPassword.setValue("xxxxxxxxxx");
            confirmPassword.setEnabled(false);
        }
    }

    /**
     * Setter for the isEditMode field.
     * @param isEditMode
     */
    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    /**
     * Setter for isAdmin toggle control enabled
     * @param isEnabled true to enable the toggle, false to disable
     */
    public void setIsAdminToggleEnabled(boolean isEnabled) {
        isAdmin.setEnabled(isEnabled);
    }

    /**
     * Setter for the isAdmin toggle control value
     * @param isAdmin true to set toggle to true, false to set toggle to false
     */
    public void setIsAdminToggleValue(boolean isAdmin) {
        this.isAdmin.setValue(isAdmin);
    }

    /**
     * Event to emit when back button is clicked
     */
    public static class BackEvent extends ComponentEvent<GarageEmployeeRegisterForm> {
        BackEvent(GarageEmployeeRegisterForm source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when next button is clicked
     */
    public static class NextEvent extends ComponentEvent<GarageEmployeeRegisterForm> {
        NextEvent(GarageEmployeeRegisterForm source) {
            super(source, false);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
