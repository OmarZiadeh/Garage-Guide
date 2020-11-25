package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

/**
 * This class is a VerticalLayout for maintaining the service categories for a garage
 */
@SuppressWarnings("FieldCanBeLocal")
public class GarageCategoryForm extends VerticalLayout {
    private final Garage garage;
    private final ServiceCatalogService serviceCatalogService;
    private final GridPro<ServiceCategory> grid = new GridPro<>(ServiceCategory.class);
    private final Button addButton = new Button("Add Category");
    private final Button exitButton = new Button("Exit Edit Form");

    /**
     * Constructor for the form
     * @param serviceCatalogService the service class for the serviceCategory repository
     * @param garage                the garage the serviceCategory is associated with
     */
    public GarageCategoryForm(ServiceCatalogService serviceCatalogService, Garage garage) {
        this.serviceCatalogService = serviceCatalogService;
        this.garage = garage;

        addClassName("garage-category-edit-form");
        setSizeFull();

        // centers the form contents within the window
        setAlignItems(Alignment.CENTER);

        // populate the service categories in the grid
        grid.setItems(serviceCatalogService.findCategoriesByGarage(garage));

        // configure the categories-grid
        grid.addClassName("categories-grid");
        grid.setEnterNextRow(true);
        grid.setHeightByRows(true);
        grid.removeAllColumns();

        // add the ServiceCategory name column as editable
        grid.addEditColumn(ServiceCategory::getCategoryName)
                .text(ServiceCategory::setCategoryName)
                .setHeader("Category Name");

        // create the delete button component
        grid.addComponentColumn(this::createDeleteButton).setHeader("Delete")
                .setTextAlign(ColumnTextAlign.END);

        // saves the serviceCategory name if it has been changed
        grid.addItemPropertyChangedListener(serviceCategoryItemPropertyChangedEvent
                -> serviceCatalogService.saveServiceCategory(serviceCategoryItemPropertyChangedEvent.getItem()));

        // button listeners
        addButton.addClickListener(e -> addCategoryClick());
        exitButton.addClickListener(e -> fireEvent(new ExitEvent(this)));

        // sets column widths
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // populates the vertical layout
        add(new HorizontalLayout(addButton, exitButton), grid);
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateCategoriesList() {
        grid.setItems(serviceCatalogService.findCategoriesByGarage(garage));
    }

    /**
     * Creates a new generic category if the add button has been clicked
     */
    private void addCategoryClick() {
        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setCategoryName("  ");
        serviceCategory.setGarage(garage);
        serviceCatalogService.saveServiceCategory(serviceCategory);
        updateCategoriesList();
    }

    /**
     * Creates delete button component
     *
     * @param category The service category the button is associated with
     * @return the created button
     */
    private Button createDeleteButton(ServiceCategory category) {
        Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create(), buttonClickEvent ->
                deleteCategoryClick(category));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        if (!serviceCatalogService.findByServiceCategory(category).isEmpty())
            deleteButton.setEnabled(false);
        return deleteButton;
    }

    /**
     * Fired on deleteServiceButton click. Shows a confirm dialog and then deletes the selected service
     */
    private void deleteCategoryClick(ServiceCategory selectedCategory) {

        String message = "Are you sure you want to delete this category?";
        ConfirmDialog confirmDeleteDialog = new ConfirmDialog(
                "Delete Category", message,
                "Delete",
                e -> onDeleteConfirm(selectedCategory),
                "Cancel",
                e -> e.getSource().close());

        confirmDeleteDialog.setConfirmButtonTheme("error primary");
        confirmDeleteDialog.open();
    }

    /**
     * Fired when delete confirm dialog is confirmed by user. Deletes serviceCategory.
     *
     * @param serviceCategory to delete.
     */
    private void onDeleteConfirm(ServiceCategory serviceCategory) {
        if (serviceCategory != null) {
            serviceCatalogService.deleteServiceCategory(serviceCategory);
            updateCategoriesList();
            String successMessage = "Deleted category " + serviceCategory.getCategoryName();

            Notification notification = new Notification(
                    successMessage,
                    4000,
                    Notification.Position.TOP_END
            );
            notification.open();
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when exit button is clicked
     */
    public static class ExitEvent extends ComponentEvent<GarageCategoryForm> {
        ExitEvent(GarageCategoryForm source) {
            super(source, false);
        }
    }
}
