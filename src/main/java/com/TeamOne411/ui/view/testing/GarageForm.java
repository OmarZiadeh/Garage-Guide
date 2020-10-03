package com.TeamOne411.ui.view.testing;

import com.TeamOne411.backend.entity.Garage;
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

public class GarageForm extends FormLayout {
    TextField companyName = new TextField("Company name");
    TextField phoneNumber = new TextField("Phone number");
    TextField address = new TextField("Address");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Garage> binder = new BeanValidationBinder<>(Garage.class);
    private Garage garage;

    public GarageForm() {
        addClassName("garage-form");
        binder.bindInstanceFields(this);
        add(companyName,
                phoneNumber,
                address,
                createButtonsLayout());
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
        binder.readBean(garage);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, garage)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(garage);
            fireEvent(new SaveEvent(this, garage));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class GarageFormEvent extends ComponentEvent<GarageForm> {
        private Garage garage;

        protected GarageFormEvent(GarageForm source, Garage garage) {
            super(source, false);
            this.garage = garage;
        }

        public Garage getGarage() {
            return garage;
        }
    }

    public static class SaveEvent extends GarageFormEvent {
        SaveEvent(GarageForm source, Garage garage) {
            super(source, garage);
        }
    }

    public static class DeleteEvent extends GarageFormEvent {
        DeleteEvent(GarageForm source, Garage garage) {
            super(source, garage);
        }
    }

    public static class CloseEvent extends GarageFormEvent {
        CloseEvent(GarageForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
