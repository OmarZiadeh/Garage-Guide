package com.TeamOne411.ui.view.garage.childview;


import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.garage.form.GarageServiceEditorDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Comparator;
import java.util.Optional;

public class GarageServicesView extends VerticalLayout {
    ServiceCatalogService serviceCatalogService;

    private Grid<OfferedService> grid = new Grid<>(OfferedService.class);
    private Garage garage;
    private Duration duration;
  //  private GarageCategoryEditorDialog garageCategoryEditorDialog;
    private GarageServiceEditorDialog garageServiceEditorDialog;

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

        //Format Service Category
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
        addServiceButton.addClickListener(e -> showNewServiceDialog());

        Button editServiceButton = new Button("Edit Service");
        editServiceButton.addClickListener(e -> showEditServiceDialog());
        editServiceButton.setEnabled(false);

        Button deleteServiceButton = new Button("Delete Service");
        deleteServiceButton.addClickListener(e -> deleteServiceClick());
        deleteServiceButton.setEnabled(false);

        grid.addSelectionListener(e -> {
            editServiceButton.setEnabled(!grid.getSelectedItems().isEmpty());
            deleteServiceButton.setEnabled(!grid.getSelectedItems().isEmpty());
        });

        add(
                new HorizontalLayout(addServiceButton, editServiceButton, deleteServiceButton),
                grid
        );

        updateServiceList();
    }

    /**
     * Calls the ServiceCatalogService to refresh the list of services. Call this anytime the services may have been edited.
     */
    private void updateServiceList() {
        grid.setItems(serviceCatalogService.findByServiceCategory_Garage(garage));
    }


    /**
     * Creates and opens a new GarageServiceEditorDialog instance not in edit mode (for adding a new service)
     */
    private void showNewServiceDialog() {
        garageServiceEditorDialog = new GarageServiceEditorDialog(serviceCatalogService, garage);

        garageServiceEditorDialog.setWidth("250px");
        garageServiceEditorDialog.setWidth("750px");

        garageServiceEditorDialog.addListener(GarageServiceEditorDialog.AddServiceSuccessEvent.class, this::onServiceAddedSuccess);
        garageServiceEditorDialog.open();
    }

    /**
     * Gets the selected service from the grid, passes it to a new GarageServiceEditorDialog instance and opens it in Edit Mode.
     */
    private void showEditServiceDialog() {
        Optional<OfferedService> selectedService = grid.getSelectedItems().stream().findFirst();

        if (selectedService.isPresent()) {
            garageServiceEditorDialog = new GarageServiceEditorDialog(serviceCatalogService, selectedService.get());

            garageServiceEditorDialog.setWidth("250px");
            garageServiceEditorDialog.setWidth("750px");

            garageServiceEditorDialog.addListener(GarageServiceEditorDialog.EditServiceSuccessEvent.class, this::onServiceEditedSuccess);
            garageServiceEditorDialog.open();
        }
    }

    /**
     * Fired on deleteServiceButton click. Shows a confirm dialog and then deletes the selected service
     */
    private void deleteServiceClick() {
        Optional<OfferedService> selectedService = grid.getSelectedItems().stream().findFirst();

        if (selectedService.isPresent()) {
            String message = "Are you sure you want to delete this service?";

            ConfirmDialog confirmDeleteDialog = new ConfirmDialog(
                    "Delete Service", message,
                    "Delete",
                    e -> onDeleteConfirm(selectedService.get()),
                    "Cancel",
                    e -> e.getSource().close());

            confirmDeleteDialog.setConfirmButtonTheme("error primary");
            confirmDeleteDialog.open();
        }
    }

    /**
     * Fired when delete confirm dialog is confirmed by user. Deletes offeredService.
     * @param offeredService to delete.
     */
    private void onDeleteConfirm(OfferedService offeredService) {
        if (offeredService != null) {
            serviceCatalogService.deleteOfferedService(offeredService);
            offeredServiceUpdates("Deleted service " + offeredService.getServiceName());
        }
    }

    /**
     * Fired when a new service is successfully added
     * @param event the event that fired this method
     */
    private void onServiceAddedSuccess(ComponentEvent<GarageServiceEditorDialog> event) {
     //  OfferedService newService = event.getSource().getOfferedService();
        garageServiceEditorDialog.close();

      //  if (newEmployee != null) {
            offeredServiceUpdates("Successfully added new service ");// + newEmployee.getFullName());
     //   }
    }


    /**
     * Fired when an existing service is successfully edited using the service editor dialog.
     * @param event the event that fired this method
     */
    private void onServiceEditedSuccess(ComponentEvent<GarageServiceEditorDialog> event) {
    //    OfferedService newService = event.getSource().getOfferedService();
        garageServiceEditorDialog.close();

        //  if (newEmployee != null) {
        offeredServiceUpdates("Successfully edited service ");// + newEmployee.getFullName());
        //   }
    }


    /**
     * This method includes some common functionality when any change to an offered service occurs
     * @param successMessage the message text to display to the user in a notification
     */
    private void offeredServiceUpdates(String successMessage) {
        updateServiceList();

        Notification notification = new Notification(
                successMessage,
                4000,
                Notification.Position.TOP_END
        );

        notification.open();
    }
}