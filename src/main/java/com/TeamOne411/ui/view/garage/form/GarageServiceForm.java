package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.utils.PriceConverter;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.time.Duration;

/**
 * This class is a VerticalLayout for adding/editing offered services for a garage
 */
@SuppressWarnings("FieldCanBeLocal")
public class GarageServiceForm extends VerticalLayout {
    Binder<OfferedService> binder = new BeanValidationBinder<>(OfferedService.class);
    private final TextField serviceName = new TextField("Service name");
    private final TextField price = new TextField("Price");
    private final ComboBox<Duration> duration = new ComboBox<>("Time Required");
    private final ComboBox<ServiceCategory> serviceCategory = new ComboBox<>("Category");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private OfferedService offeredService = new OfferedService();

    /**
     * Constructor for the form
     *
     * @param serviceCatalogService the service class for the offeredService repository
     * @param garage                the service class for the garage repository
     */
    public GarageServiceForm(ServiceCatalogService serviceCatalogService, Garage garage) {

        // initial view setup
        addClassName("garage-service-form");
        serviceName.setWidth("100%");

        // centers the form contents within the window
        setAlignItems(Alignment.CENTER);

        // format Price
        binder.forField(price).withNullRepresentation("").withConverter(new PriceConverter()).bind("price");
        price.setWidth("100%");
        binder.bindInstanceFields(this);

        // set button click listeners
        saveButton.addClickListener(e -> fireEvent(new SaveEvent(this)));
        cancelButton.addClickListener(e -> fireEvent(new CancelEvent(this)));

        // format Service Category
        serviceCategory.setItemLabelGenerator(ServiceCategory::getCategoryName);
        serviceCategory.setItems(serviceCatalogService.findCategoriesByGarage(garage));
        serviceCategory.setWidth("100%");

        // format Duration
        // TODO fix duration formatting
        duration.setItems((Duration.ofMinutes(0)), Duration.ofMinutes(30), Duration.ofMinutes(60),
                Duration.ofMinutes(90), Duration.ofMinutes(120),
                Duration.ofMinutes(150), Duration.ofMinutes(180));
        duration.setWidth("100%");

        // add fields to the form
        add(serviceName,
                price,
                duration,
                serviceCategory,
                new HorizontalLayout(saveButton, cancelButton));
    }

    /**
     * Fills form with known details of an existing service.
     *
     * @param service the offeredService to fill details for
     */
    public void prefillForm(OfferedService service) {
        offeredService = service;
        binder.readBean(service);
    }

    /**
     * Attempts to return an offered service instance from the values of the form controls.
     *
     * @return an offered service instance
     */
    public OfferedService getOfferedService() {
        try {
            binder.writeBean(offeredService);
            return offeredService;
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when save button is clicked
     */
    public static class SaveEvent extends ComponentEvent<GarageServiceForm> {
        SaveEvent(GarageServiceForm source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when cancel button is clicked
     */
    public static class CancelEvent extends ComponentEvent<GarageServiceForm> {
        CancelEvent(GarageServiceForm source) {
            super(source, false);
        }
    }
}
