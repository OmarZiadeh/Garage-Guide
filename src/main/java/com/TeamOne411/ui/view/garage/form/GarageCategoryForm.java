package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class GarageCategoryForm extends VerticalLayout {
    private TextField categoryName = new TextField("Category name");
    private Garage garage;
    private Button saveButton = new Button("Save");
    private Button cancelButton = new Button("Cancel");
    private ServiceCatalogService serviceCatalogService;

    Binder<ServiceCategory> binder = new BeanValidationBinder<>(ServiceCategory.class);
    private ServiceCategory serviceCategory = new ServiceCategory();

    public GarageCategoryForm(Garage garage, ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
        this.garage = garage;

        // initial view setup
        addClassName("garage-category-form");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        binder.bindInstanceFields(this);

        // set button click listeners
        saveButton.addClickListener(e -> fireEvent(new SaveEvent(this)));
        cancelButton.addClickListener(e -> fireEvent(new CancelEvent(this)));

        // add fields to form
        add(categoryName, new HorizontalLayout(saveButton, cancelButton));
    }

    /**
     * Event to emit when save button is clicked
     */
    public static class SaveEvent extends ComponentEvent<GarageCategoryForm> {
        SaveEvent(GarageCategoryForm source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when cancel button is clicked
     */
    public static class CancelEvent extends ComponentEvent<GarageCategoryForm> {
        CancelEvent(GarageCategoryForm source) {
            super(source, false);
        }
    }

    /**
     * Fills all form controls with known details of an existing category.
     */
    public void prefillForm(ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
        binder.readBean(serviceCategory);
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


}
