package com.TeamOne411.ui.view.sandbox.form;

import com.TeamOne411.backend.entity.users.CarOwner;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class CarOwnerEditorForm extends FormLayout {
    private TextField username = new TextField("Username");
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField emailAddress = new TextField("Email Address");
    private TextField phoneNumber = new TextField("Phone Number");
    private TextField address = new TextField("Address");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");

    Binder<CarOwner> binder = new BeanValidationBinder<>(CarOwner.class);
    private CarOwner carOwner = new CarOwner();

    public CarOwnerEditorForm() {
        addClassName("garage-employee-form");
        binder.bindInstanceFields(this);

        add(username,
                firstName,
                lastName,
                emailAddress,
                phoneNumber,
                address,
                createButtonsLayout());
    }

    public void setCarOwner(CarOwner carOwner) {
        this.carOwner = carOwner;
        binder.readBean(carOwner);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, carOwner)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(carOwner);
            fireEvent(new SaveEvent(this, carOwner));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class CarOwnerFormEvent extends ComponentEvent<CarOwnerEditorForm> {
        private CarOwner carOwner;

        protected CarOwnerFormEvent(CarOwnerEditorForm source, CarOwner carOwner) {
            super(source, false);
            this.carOwner = carOwner;
        }

        public CarOwner getCarOwner() {
            return carOwner;
        }
    }

    public static class SaveEvent extends CarOwnerFormEvent {
        SaveEvent(CarOwnerEditorForm source, CarOwner carOwner) {
            super(source, carOwner);
        }
    }

    public static class DeleteEvent extends CarOwnerFormEvent {
        DeleteEvent(CarOwnerEditorForm source, CarOwner carOwner) {
            super(source, carOwner);
        }
    }

    public static class CloseEvent extends CarOwnerFormEvent {
        CloseEvent(CarOwnerEditorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
