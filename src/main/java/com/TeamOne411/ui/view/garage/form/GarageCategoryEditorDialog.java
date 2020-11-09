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
     * This constructor is for the dialog to create a new category.
     * @param serviceCatalogService
     * @param garage
     */
    public GarageCategoryEditorDialog(ServiceCatalogService serviceCatalogService, Garage garage) {
        this.serviceCatalogService = serviceCatalogService;
        this.garage = garage;

        initDialog("Add New Category");
    }

    /**
     * This constructor is for the dialog to edit an existing category.
     * @param serviceCatalogService
     * @param serviceCategory
     */
    public GarageCategoryEditorDialog(ServiceCatalogService serviceCatalogService, ServiceCategory serviceCategory) {
        this.serviceCatalogService = serviceCatalogService;
        this.serviceCategory = serviceCategory;
        this.garage = serviceCategory.getGarage();

        initDialog("Edit Category");
        garageCategoryForm.prefillForm(serviceCategory);
    }

    /**
     * Initializes the dialog with some common functions. Should be called by all constructors.
     * @param title The title text to give to the dialog
     */
    private void initDialog(String title) {
        garageCategoryForm = new GarageCategoryForm(garage, serviceCatalogService);
  //      garageCategoryForm.addListener(GarageCategoryForm.CancelEvent.class, this::onCancelClick);
  //      garageCategoryForm.addListener(GarageCategoryForm.SaveEvent.class, this::onComplete);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        container.add(new H3(title), garageCategoryForm);
        add(container);
    }
}
