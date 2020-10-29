package com.TeamOne411.ui.view.sandbox.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.sandbox.form.CategoryEditorForm;
import com.TeamOne411.ui.view.sandbox.form.OfferedServiceEditorForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.DecimalFormat;
import java.util.Comparator;

/**
 * The ServiceCatalogSandboxView is a God-mode ServiceCatalog editor for testing purposes.
 * This should either be deleted as the application evolves or turned into an Admin screen meant only for technical support, well hidden behind authentication.
 */
public class ServiceCatalogSandboxView extends VerticalLayout {
    private Grid<OfferedService> grid = new Grid<>(OfferedService.class);
    private ServiceCatalogService serviceCatalogService;
    private GarageService garageService;
    private OfferedServiceEditorForm serviceEditorForm = new OfferedServiceEditorForm();
    private CategoryEditorForm categoryEditorForm = new CategoryEditorForm();
    private Button addServiceButton = new Button("Add Service");
    private Button addCategoryButton = new Button("Add Category");

    /**
     * The constructor for the sandbox view. Does initial layout setup, grid configuration, and event listener attachment
     * @param serviceCatalogService the GarageEmployeeService to broker the repository calls for garage employees
     * @param garageService the GarageService to broker the repository calls for garages
     */
    public ServiceCatalogSandboxView(ServiceCatalogService serviceCatalogService, GarageService garageService) {
        //initial layout setup
        this.serviceCatalogService = serviceCatalogService;
        this.garageService = garageService;
        addClassName("list-view");
        setSizeFull();

        //configure the service-catalog-grid
        grid.addClassName("service-catalog-grid");
        grid.setHeightByRows(true);
        //TODO add serviceDescription back in if we decide to keep it
        grid.setColumns("serviceName"); //, "serviceDescription");

        grid.addColumn(offeredService -> {
            ServiceCategory serviceCategory = offeredService.getServiceCategory();
            return serviceCategory.getCategoryName();
        }).setSortable(true).setHeader("Category").setKey("serviceCategory");

        //Format price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        grid.addColumn(offeredService -> decimalFormat.format(offeredService.getPrice()))
                .setHeader("Price").setComparator(Comparator.comparing(OfferedService::getPrice))
                .setKey("price");

        //TODO add duration in once CRUD operations for duration are completed
        //grid.addColumn(OfferedService::getDuration).setHeader("Duration").setSortable(true).setKey("duration");

        //add garage
        grid.addColumn(offeredService -> {
            Garage garage = offeredService.getGarage();
            return garage == null ? "[None]" : garage.getCompanyName();
        }).setSortable(true).setHeader("Garage");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // attach event listener on grid item select
        grid.asSingleSelect().addValueChangeListener(event -> editOfferedService(event.getValue()));

        // connect the handlers to the form events
        serviceEditorForm.addListener(OfferedServiceEditorForm.SaveEvent.class, this::saveOfferedService);
        serviceEditorForm.addListener(OfferedServiceEditorForm.DeleteEvent.class, this::deleteOfferedService);
        serviceEditorForm.addListener(OfferedServiceEditorForm.CloseEvent.class, this::closeOfferedServiceFormHandler);

        categoryEditorForm.addListener(CategoryEditorForm.SaveEvent.class, this::saveServiceCategory);
        categoryEditorForm.addListener(CategoryEditorForm.DeleteEvent.class, this::deleteServiceCategory);
        categoryEditorForm.addListener(CategoryEditorForm.CloseEvent.class, this::closeCategoryFormHandler);

        // set a click lister to the add button to show the service form and then hide the add button
        addServiceButton.addClickListener(event -> {
            serviceEditorForm.setVisible(true);
            addServiceButton.setVisible(false);
        });

        // set a click lister to the add button to show the category form and then hide the add button
        addCategoryButton.addClickListener(event -> {
            categoryEditorForm.setVisible(true);
            addCategoryButton.setVisible(false);
        });

        // set the forms' default visibility to false
        serviceEditorForm.setVisible(false);
        categoryEditorForm.setVisible(false);

        // build a div element with title, serviceCatalog grid, and editor forms
        Div offeredServiceContent = new Div(
                grid,
                serviceEditorForm,
                categoryEditorForm
        );

        offeredServiceContent.addClassName("offeredServiceContent");
        offeredServiceContent.setSizeFull();

        // add the components to this layout
        add(new H1("Services"), addCategoryButton, addServiceButton, offeredServiceContent);

        // fetch the list for the grid
        updateOfferedServicesList();

        // pass down garages to the form for the garage combobox
        updateGarageCombobox();

        // pass down categories to the form for the categories combobox
        updateCategoriesCombobox();
    }

    private void updateGarageCombobox() {
        serviceEditorForm.setGarages(garageService.findAll());
        categoryEditorForm.setGarages(garageService.findAll());
    }

    private void updateCategoriesCombobox() {
        serviceEditorForm.setServiceCategories(serviceCatalogService.findAllServiceCategories());
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateOfferedServicesList() {
        grid.setItems(serviceCatalogService.findAllOfferedServices());
    }

    /**
     * Saves the given offeredService to the database
     * @param event the SaveEvent from the offeredService editor form
     */
    private void saveOfferedService(OfferedServiceEditorForm.SaveEvent event) {
        serviceCatalogService.saveOfferedService(event.getOfferedService());
        updateOfferedServicesList();
        closeOfferedServiceEditorForm();
        // TODO add toast to confirm add
    }

    /**
     * Saves the given serviceCategory to the database
     * @param event the SaveEvent from the serviceCategory editor form
     */
    private void saveServiceCategory(CategoryEditorForm.SaveEvent event) {
        serviceCatalogService.saveServiceCategory(event.getServiceCategory());
        closeCategoryEditorForm();
        // TODO add toast to confirm add
    }


    /**
     * Deletes the given offeredService from the database.
     * @param event the DeleteEvent from the offeredService editor form
     */
    private void deleteOfferedService(OfferedServiceEditorForm.DeleteEvent event) {
        if (event.getOfferedService() != null) {
            // TODO make this run only after confirm delete dialog
            serviceCatalogService.deleteOfferedService(event.getOfferedService());
            updateOfferedServicesList();
        }
        closeOfferedServiceEditorForm();
    }

    /**
     * Deletes the given serviceCategory from the database.
     * @param event the DeleteEvent from the serviceCategory editor form
     */
    private void deleteServiceCategory(CategoryEditorForm.DeleteEvent event) {
        if (event.getServiceCategory() != null) {
            // TODO make this run only after confirm delete dialog
            serviceCatalogService.deleteServiceCategory(event.getServiceCategory());
        }
        closeCategoryEditorForm();
    }

    /**
     * Toggles the form visibility and sets initializes form fields if passed a OfferedService instance
     * @param offeredService the OfferedService instance to edit, or null if none is selected
     */
    private void editOfferedService(OfferedService offeredService) {
        if (offeredService == null) {
            closeOfferedServiceEditorForm();
        } else {
            serviceEditorForm.setOfferedService(offeredService);
            serviceEditorForm.setVisible(true);
            addClassName("editing-Service");
        }
    }

    /**
     * Toggles the form visibility and sets initializes form fields if passed a ServiceCategory instance
     * @param serviceCategory the ServiceCategory instance to edit, or null if none is selected
     */
    private void editServiceCategory(ServiceCategory serviceCategory) {
        if (serviceCategory == null) {
            closeCategoryEditorForm();
        } else {
            categoryEditorForm.setServiceCategory(serviceCategory);
            categoryEditorForm.setVisible(true);
            addClassName("editing-Category");
        }
    }

    /**
     * Clears and hides the editor form
     */
    private void closeOfferedServiceEditorForm() {
        serviceEditorForm.setOfferedService(new OfferedService());
        serviceEditorForm.setVisible(false);
        removeClassName("editing-Service");
        addServiceButton.setVisible(true);
    }

    /**
     * Clears and hides the editor form
     */
    private void closeCategoryEditorForm() {
        categoryEditorForm.setServiceCategory(new ServiceCategory());
        categoryEditorForm.setVisible(false);
        removeClassName("editing-Service");
        addCategoryButton.setVisible(true);
    }

    /**
     * Clears and hides the editor form
     */
    private void closeOfferedServiceFormHandler(OfferedServiceEditorForm.CloseEvent event) {
        if (event.getOfferedService() != null)
        {
            // TODO add confirm dialog
        }
        closeOfferedServiceEditorForm();
    }

    /**
     * Clears and hides the editor form
     */
    private void closeCategoryFormHandler(CategoryEditorForm.CloseEvent event) {
        if (event.getServiceCategory() != null)
        {
            // TODO add confirm dialog
        }
        closeCategoryEditorForm();
    }
}