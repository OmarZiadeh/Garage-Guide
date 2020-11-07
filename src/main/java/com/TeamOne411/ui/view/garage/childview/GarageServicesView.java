package com.TeamOne411.ui.view.garage.childview;


import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.garage.form.CategoryEditorDialog;
import com.TeamOne411.ui.view.garage.form.ServiceEditorDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GarageServicesView extends VerticalLayout {
    ServiceCatalogService serviceCatalogService;

    private Grid<OfferedService> grid = new Grid<>(OfferedService.class);
    private Garage garage;
    private CategoryEditorDialog categoryEditorDialog;
    private ServiceEditorDialog serviceEditorDialog;







}