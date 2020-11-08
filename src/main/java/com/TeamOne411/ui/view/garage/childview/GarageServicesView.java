package com.TeamOne411.ui.view.garage.childview;


import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.garage.form.CategoryEditorDialog;
import com.TeamOne411.ui.view.garage.form.ServiceEditorDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GarageServicesView extends VerticalLayout {
    ServiceCatalogService serviceCatalogService;

    private Grid<OfferedService> grid = new Grid<>(OfferedService.class);
    private Garage garage;
    private Duration duration;
    private CategoryEditorDialog categoryEditorDialog;
    private ServiceEditorDialog serviceEditorDialog;

    public GarageServicesView(
            ServiceCatalogService serviceCatalogService,
            Garage garage
    ) {
        this.serviceCatalogService = serviceCatalogService;
        this.garage = garage;

        // configure the service grid
        grid.addClassName("garage-service-grid");
        grid.setHeightByRows(true);
        grid.setMaxHeight("25vh");
        grid.setColumns("serviceName");

        grid.addColumn(offeredService -> {
            ServiceCategory serviceCategory = offeredService.getServiceCategory();
            return serviceCategory.getCategoryName();
        }).setSortable(true).setHeader("Category").setKey("serviceCategory");

        //Format price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        grid.addColumn(offeredService -> decimalFormat.format(offeredService.getPrice()))
                .setHeader("$ Price").setComparator(Comparator.comparing(OfferedService::getPrice))
                .setKey("price");

        //Format Duration
        grid.addColumn(offeredService -> {
            duration = offeredService.getDuration();
            return duration.toMinutes();
        }).setHeader("Duration in Minutes").setSortable(true).setKey("duration");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        Button addServiceButton = new Button("Add New Service");
       // addServiceButton.addClickListener(e -> showNewEmployeeDialog());

        Button editServiceButton = new Button("Edit Service");
     //   editServiceButton.addClickListener(e -> showEditEmployeeDialog());
        editServiceButton.setEnabled(false);

        Button deleteServiceButton = new Button("Delete Service");
     //   deleteEmployeeButton.addClickListener(e -> deleteEmployeeClick());
        deleteServiceButton.setEnabled(false);

        Button addCategoryButton = new Button("Add New Category");
        // addCategoryButton.addClickListener(e -> showNewEmployeeDialog());

        Button editCategoryButton = new Button("Edit Category");
        //   editCategoryButton.addClickListener(e -> showEditEmployeeDialog());
        editCategoryButton.setEnabled(false);

        Button deleteCategoryButton = new Button("Delete Category");
        //   deleteCategoryButton.addClickListener(e -> deleteEmployeeClick());
        deleteCategoryButton.setEnabled(false);

        grid.addSelectionListener(e -> {
            editServiceButton.setEnabled(!grid.getSelectedItems().isEmpty());
            deleteServiceButton.setEnabled(!grid.getSelectedItems().isEmpty());
            editCategoryButton.setEnabled(!grid.getSelectedItems().isEmpty());
            deleteCategoryButton.setEnabled(!grid.getSelectedItems().isEmpty());
        });

        add(
                new HorizontalLayout(addServiceButton, editServiceButton, deleteServiceButton, addCategoryButton, editCategoryButton, deleteCategoryButton),
                grid
        );

        updateServiceList();
    }

    /**
     * Calls the ServiceCatalogService to refresh the list of services. Call this anytime the services may have been edited.
     */
    private void updateServiceList() {
        List<ServiceCategory> serviceCategoryList = new ArrayList<>();
        serviceCategoryList.addAll(serviceCatalogService.findCategoriesByGarage(garage));

        List<OfferedService> offeredServices = new ArrayList<>();
        for(ServiceCategory sc : serviceCategoryList){
            offeredServices.addAll(serviceCatalogService.findServicesByServiceCategory(sc));
        }

        grid.setItems(offeredServices);
    }
}