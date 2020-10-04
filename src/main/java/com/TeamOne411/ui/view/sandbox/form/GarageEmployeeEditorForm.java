package com.TeamOne411.ui.view.sandbox.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class GarageEmployeeEditorForm extends FormLayout {
    private TextField userName = new TextField("Username");
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private Checkbox isAdmin = new Checkbox("Garage Admin");
    private ComboBox<Garage> garage = new ComboBox<>("Employer");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");

    Binder<GarageEmployee> binder = new BeanValidationBinder<>(GarageEmployee.class);
    private GarageEmployee garageEmployee = new GarageEmployee();

    public GarageEmployeeEditorForm() {
        addClassName("garage-employee-form");
        binder.bindInstanceFields(this);
        garage.setItemLabelGenerator(Garage::getCompanyName);
        add(userName,
                firstName,
                lastName,
                garage,
                isAdmin,
                createButtonsLayout());
    }

    public void setGarages(List<Garage> garages) {
        this.garage.setItems(garages);
    }

    public void setGarageEmployee(GarageEmployee garageEmployee) {
        this.garageEmployee = garageEmployee;
        binder.readBean(garageEmployee);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, garageEmployee)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(garageEmployee);
            fireEvent(new SaveEvent(this, garageEmployee));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class GarageEmployeeFormEvent extends ComponentEvent<GarageEmployeeEditorForm> {
        private GarageEmployee garageEmployee;

        protected GarageEmployeeFormEvent(GarageEmployeeEditorForm source, GarageEmployee garageEmployee) {
            super(source, false);
            this.garageEmployee = garageEmployee;
        }

        public GarageEmployee getGarageEmployee() {
            return garageEmployee;
        }
    }

    public static class SaveEvent extends GarageEmployeeFormEvent {
        SaveEvent(GarageEmployeeEditorForm source, GarageEmployee garageEmployee) {
            super(source, garageEmployee);
        }
    }

    public static class DeleteEvent extends GarageEmployeeFormEvent {
        DeleteEvent(GarageEmployeeEditorForm source, GarageEmployee garageEmployee) {
            super(source, garageEmployee);
        }
    }

    public static class CloseEvent extends GarageEmployeeFormEvent {
        CloseEvent(GarageEmployeeEditorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
