package com.TeamOne411.ui.view.sandbox.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.ui.utils.PriceConverter;
import com.TeamOne411.ui.view.sandbox.childview.ServicesSandboxView;
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

import java.time.Duration;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class OfferedServiceEditorForm extends FormLayout {
    Binder<OfferedService> binder = new BeanValidationBinder<>(OfferedService.class);
    private final TextField serviceName = new TextField("Service name");
    private final ComboBox<Garage> garageComboBox = new ComboBox<>("Garage");
    private final TextField price = new TextField("$ Price");
    private final ComboBox<Duration> duration = new ComboBox<>("Duration");
    private final ComboBox<ServiceCategory> serviceCategory = new ComboBox<>("Category");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button close = new Button("Cancel");
    private OfferedService offeredService = new OfferedService();

    public OfferedServiceEditorForm(ServicesSandboxView servicesSandboxView) {

        // SET FORM ATTRIBUTES
        addClassName("offered-service-form");
        binder.forField(price).withNullRepresentation("").withConverter(new PriceConverter()).bind("price");
        binder.bindInstanceFields(this);
        garageComboBox.setItemLabelGenerator(Garage::getCompanyName);
        serviceCategory.setItemLabelGenerator(ServiceCategory::getCategoryName);
        duration.setItems((Duration.ofMinutes(0)), Duration.ofMinutes(30), Duration.ofMinutes(60),
                Duration.ofMinutes(90), Duration.ofMinutes(120),
                Duration.ofMinutes(150), Duration.ofMinutes(180));

        // LISTENERS
        garageComboBox.addValueChangeListener(comboBoxGarageComponentValueChangeEvent ->
                servicesSandboxView.setGarage(garageComboBox.getValue()));

        // ADD FIELDS TO FORM
        add(serviceName,
                garageComboBox,
                price,
                duration,
                serviceCategory,
                createButtonsLayout());
    }

    public void setGarages(List<Garage> garages) {
        this.garageComboBox.setItems(garages);
    }

    public void setServiceCategories(List<ServiceCategory> categories) {
        this.serviceCategory.setItems(categories);
    }

    public void setOfferedService(OfferedService offeredService) {
        this.offeredService = offeredService;
        garageComboBox.setValue(offeredService.getServiceCategory().getGarage());
        binder.readBean(offeredService);
    }

    public void clearOfferedService(OfferedService offeredService) {
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

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class OfferedServiceFormEvent extends ComponentEvent<OfferedServiceEditorForm> {
        private final OfferedService offeredService;

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
}