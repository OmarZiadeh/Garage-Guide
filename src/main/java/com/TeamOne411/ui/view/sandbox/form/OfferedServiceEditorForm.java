package com.TeamOne411.ui.view.sandbox.form;
import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class OfferedServiceEditorForm extends FormLayout {
    private TextField serviceName = new TextField("Service name");
    private TextField description = new TextField("Description");
    private ComboBox<Garage> garage = new ComboBox<>("Garage");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");

    Binder<OfferedService> binder = new BeanValidationBinder<>(OfferedService.class);
    private OfferedService offeredService = new OfferedService();

    public OfferedServiceEditorForm() {
        addClassName("service-catalog-form");
        binder.bindInstanceFields(this);
        garage.setItemLabelGenerator(Garage::getCompanyName);
        add(serviceName,
                description,
                createButtonsLayout());
    }

    public void setGarages(List<Garage> garages) {
        this.garage.setItems(garages);
    }

    public void setOfferedService(OfferedService offeredService) {
        this.offeredService = offeredService;
        binder.readBean(offeredService);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, offeredService)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(offeredService);
            fireEvent(new SaveEvent(this, offeredService));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class OfferedServiceFormEvent extends ComponentEvent<OfferedServiceEditorForm> {
        private OfferedService offeredService;

        protected OfferedServiceFormEvent(OfferedServiceEditorForm source, OfferedService offeredService) {
            super(source, false);
            this.offeredService = offeredService;
        }

        public OfferedService getOfferedService() {
            return offeredService;
        }
    }

    public static class SaveEvent extends OfferedServiceFormEvent {
        SaveEvent(OfferedServiceEditorForm source, OfferedService offeredService) {
            super(source, offeredService);
        }
    }

    public static class DeleteEvent extends OfferedServiceFormEvent {
        DeleteEvent(OfferedServiceEditorForm source, OfferedService offeredService) {
            super(source, offeredService);
        }
    }

    public static class CloseEvent extends OfferedServiceFormEvent {
        CloseEvent(OfferedServiceEditorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}