package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

public class GarageCategoryForm extends VerticalLayout {
    private Garage garage;
    private ServiceCatalogService serviceCatalogService;

    Binder<ServiceCategory> binder = new BeanValidationBinder<>(ServiceCategory.class);
    private ServiceCategory serviceCategory = new ServiceCategory();

    public GarageCategoryForm(Garage garage, ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
        this.garage = garage;

    }

}
