package com.TeamOne411.ui.view.garage.form;


import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

/**
 * This class is a dialog that wraps GarageServiceForm.
 */
public class GarageServiceEditorDialog extends Dialog {
    private final ServiceCatalogService serviceCatalogService;
    private final Garage garage;
    private GarageServiceForm garageServiceForm;
    private OfferedService offeredService;
    private boolean isEditMode = false;

    /**
     * This constructor is for the dialog to create a new service.
     *
     * @param serviceCatalogService service class for the OfferedService repository
     * @param garage                service class for the Garage repository
     */
    public GarageServiceEditorDialog(ServiceCatalogService serviceCatalogService, Garage garage) {
        this.serviceCatalogService = serviceCatalogService;
        this.garage = garage;

        initDialog("Add New Service");
    }

    /**
     * This constructor is for the dialog to edit an existing employee service.
     *
     * @param serviceCatalogService service class for the OfferedService repository
     * @param offeredService        OfferedService instance to populate in the form
     */
    public GarageServiceEditorDialog(ServiceCatalogService serviceCatalogService, OfferedService offeredService) {
        this.serviceCatalogService = serviceCatalogService;
        this.offeredService = offeredService;
        this.garage = offeredService.getServiceCategory().getGarage();
        isEditMode = true;

        initDialog("Edit Service Details");
        garageServiceForm.prefillForm(offeredService);
    }

    /**
     * Initializes the dialog with some common functions. Should be called by all constructors.
     *
     * @param title The title text to give to the dialog
     */
    private void initDialog(String title) {
        garageServiceForm = new GarageServiceForm(serviceCatalogService, garage);
        garageServiceForm.addListener(GarageServiceForm.CancelEvent.class, this::onCancelClick);
        garageServiceForm.addListener(GarageServiceForm.SaveEvent.class, this::onSaveClick);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        setResizable(true);
        VerticalLayout container = new VerticalLayout();

        // Centers the title and form within the dialog window
        container.setAlignItems(FlexComponent.Alignment.CENTER);

        container.add(new H3(title), garageServiceForm);
        add(container);
    }

    /**
     * Fired when cancel button in child form is clicked. Closes the dialog.
     *
     * @param event event that occurred
     */
    private void onCancelClick(ComponentEvent event) {
        close();
    }


    /**
     * Fired when save button in child form is clicked. Propagates another event depending on Add or Edit mode.
     *
     * @param event event that occurred
     */
    private void onSaveClick(ComponentEvent event) {
        // get the offeredService
        offeredService = garageServiceForm.getOfferedService();
        serviceCatalogService.saveOfferedService(offeredService);

        if (!isEditMode) {
            fireEvent(new GarageServiceEditorDialog.AddServiceSuccessEvent(this));
        } else {
            fireEvent(new GarageServiceEditorDialog.EditServiceSuccessEvent(this));
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when a new service is successfully added.
     */
    public static class AddServiceSuccessEvent extends ComponentEvent<GarageServiceEditorDialog> {
        AddServiceSuccessEvent(GarageServiceEditorDialog source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when an existing service is successfully edited.
     */
    public static class EditServiceSuccessEvent extends ComponentEvent<GarageServiceEditorDialog> {
        EditServiceSuccessEvent(GarageServiceEditorDialog source) {
            super(source, false);
        }
    }
}
