package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * This class is a dialog that wraps GarageCategoryForm.
 */
public class GarageCategoryEditorDialog extends Dialog {
    private GarageCategoryForm garageCategoryForm;
    private ServiceCatalogService serviceCatalogService;
    private Garage garage;
    private ServiceCategory serviceCategory;

    /**
     * This constructor is for the dialog
     * @param serviceCatalogService
     * @param garage
     */
    public GarageCategoryEditorDialog(ServiceCatalogService serviceCatalogService, Garage garage) {
        this.serviceCatalogService = serviceCatalogService;
        this.garage = garage;

        garageCategoryForm = new GarageCategoryForm(garage, serviceCatalogService);
        //      garageCategoryForm.addListener(GarageCategoryForm.CancelEvent.class, this::onCancelClick);
        //      garageCategoryForm.addListener(GarageCategoryForm.SaveEvent.class, this::onComplete);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        container.add(new H3("Edit Service Categories"), garageCategoryForm);
        add(container);
    }
}
