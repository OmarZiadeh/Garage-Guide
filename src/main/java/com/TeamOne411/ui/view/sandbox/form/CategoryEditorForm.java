package com.TeamOne411.ui.view.sandbox.form;
import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
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

public class CategoryEditorForm extends FormLayout {

    private TextField categoryName = new TextField("Category name");
    private ComboBox<Garage> garage = new ComboBox<>("Garage");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");

    Binder<ServiceCategory> binder = new BeanValidationBinder<>(ServiceCategory.class);
    private ServiceCategory serviceCategory = new ServiceCategory();

    public CategoryEditorForm() {
        addClassName("service-category-form");
        binder.bindInstanceFields(this);
        garage.setItemLabelGenerator(Garage::getCompanyName);
        add(categoryName,
                garage,
                createButtonsLayout());
    }

    public void setGarages(List<Garage> garages) {
        this.garage.setItems(garages);
    }

    public void setServiceCategory(ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
        binder.readBean(serviceCategory);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new CategoryEditorForm.DeleteEvent(this, serviceCategory)));
        close.addClickListener(event -> fireEvent(new CategoryEditorForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(serviceCategory);
            fireEvent(new CategoryEditorForm.SaveEvent(this, serviceCategory));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class CategoryFormEvent extends ComponentEvent<CategoryEditorForm> {
        private ServiceCategory serviceCategory;

        protected CategoryFormEvent(CategoryEditorForm source, ServiceCategory serviceCategory) {
            super(source, false);
            this.serviceCategory = serviceCategory;
        }

        public ServiceCategory getServiceCategory() {
            return serviceCategory;
        }
    }

    public static class SaveEvent extends CategoryEditorForm.CategoryFormEvent {
        SaveEvent(CategoryEditorForm source, ServiceCategory serviceCategory) {
            super(source, serviceCategory);
        }
    }

    public static class DeleteEvent extends CategoryEditorForm.CategoryFormEvent {
        DeleteEvent(CategoryEditorForm source, ServiceCategory serviceCategory) {
            super(source, serviceCategory);
        }
    }

    public static class CloseEvent extends CategoryEditorForm.CategoryFormEvent {
        CloseEvent(CategoryEditorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
