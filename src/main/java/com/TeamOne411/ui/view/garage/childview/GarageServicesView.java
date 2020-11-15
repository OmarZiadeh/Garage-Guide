package com.TeamOne411.ui.view.garage.childview;


import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.garage.form.GarageCategoryEditorDialog;
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

/**
 * This class is a Vertical layout that shows a list of employees for a given garage and controls to add new or edit them.
 */
public class GarageServicesView extends VerticalLayout {
    ServiceCatalogService serviceCatalogService;

    private final Grid<OfferedService> grid = new Grid<>(OfferedService.class);
    private final Garage garage;
    private Duration duration;
    private GarageServiceEditorDialog garageServiceEditorDialog;
    private GarageCategoryEditorDialog garageCategoryEditorDialog;

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

        // Format Service Category
        grid.addColumn(offeredService -> {
            ServiceCategory serviceCategory = offeredService.getServiceCategory();
            return serviceCategory.getCategoryName();
        }).setSortable(true).setHeader("Category").setKey("serviceCategory").setFooter("");

        // Format price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        grid.addColumn(offeredService -> decimalFormat.format(offeredService.getPrice()))
                .setHeader("Price").setComparator(Comparator.comparing(OfferedService::getPrice))
                .setKey("price");

        // Format Duration
        // TODO fix duration formatting
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

        Button editCategoriesButton = new Button("Edit Categories");
        editCategoriesButton.addClickListener(e -> showEditCategoriesDialog());

        grid.addSelectionListener(e -> {
            editServiceButton.setEnabled(!grid.getSelectedItems().isEmpty());
            deleteServiceButton.setEnabled(!grid.getSelectedItems().isEmpty());
        });

        add(new HorizontalLayout(addServiceButton, editServiceButton, deleteServiceButton, editCategoriesButton),
                grid);
        updateServiceList();
    }

    /**
     * Calls the ServiceCatalogService to refresh the list of services.
     * Call this anytime the services or categories may have been edited.
     */
    private void updateServiceList() {
        grid.setItems(serviceCatalogService.findByServiceCategory_Garage(garage));
    }

    /**
     * Creates and opens a new GarageServiceEditorDialog instance for adding a new service
     */
    private void showNewServiceDialog() {
        garageServiceEditorDialog = new GarageServiceEditorDialog(serviceCatalogService, garage);
        garageServiceEditorDialog.setWidth("25%");
        garageServiceEditorDialog.addListener(GarageServiceEditorDialog.AddServiceSuccessEvent.class,
                this::onServiceAddedSuccess);
        garageServiceEditorDialog.open();
    }

    /**
     * Gets the selected service from the grid,
     * passes it to a new GarageServiceEditorDialog instance and opens it in Edit Mode.
     */
    private void showEditServiceDialog() {
        Optional<OfferedService> selectedService = grid.getSelectedItems().stream().findFirst();

        if (selectedService.isPresent()) {
            garageServiceEditorDialog = new GarageServiceEditorDialog(serviceCatalogService, selectedService.get());
            garageServiceEditorDialog.setWidth("25%");
            garageServiceEditorDialog.addListener(GarageServiceEditorDialog.EditServiceSuccessEvent.class,
                    this::onServiceEditedSuccess);
            garageServiceEditorDialog.open();
        }
    }

    /**
     * Creates and opens a new GarageCategoryEditorDialog instance for updating the ServiceCategories
     */
    private void showEditCategoriesDialog() {
        garageCategoryEditorDialog = new GarageCategoryEditorDialog(serviceCatalogService, garage);
        garageCategoryEditorDialog.setWidth("25%");
        garageCategoryEditorDialog.setHeight("auto");
        garageCategoryEditorDialog.addListener(GarageCategoryEditorDialog.EditCategoriesExitEvent.class,
                this::onEditCategoriesExit);
        garageCategoryEditorDialog.open();
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
        garageServiceEditorDialog.close();
        offeredServiceUpdates("Successfully added new service");
    }

    /**
     * Fired when an existing service is successfully edited using the service editor dialog.
     * @param event the event that fired this method
     */
    private void onServiceEditedSuccess(ComponentEvent<GarageServiceEditorDialog> event) {
        garageServiceEditorDialog.close();
        offeredServiceUpdates("Successfully edited service");
    }

    /**
     * Fired when the edit categories form has been exited. Refreshes the service list grid.
     * @param event the event that fired this method
     */
    private void onEditCategoriesExit(ComponentEvent<GarageCategoryEditorDialog> event) {
        garageCategoryEditorDialog.close();
        updateServiceList();
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