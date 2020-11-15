package com.TeamOne411.ui.view.sandbox.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.sandbox.form.OfferedServiceEditorForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Comparator;

/**
 * The ServicesSandboxView is a God-mode ServiceCatalog editor for testing purposes.
 * This should either be deleted as the application evolves or turned into an Admin screen meant only for technical support, well hidden behind authentication.
 */
public class ServicesSandboxView extends VerticalLayout {
    private Grid<OfferedService> grid = new Grid<>(OfferedService.class);
    private ServiceCatalogService serviceCatalogService;
    private GarageService garageService;
    private OfferedServiceEditorForm serviceEditorForm = new OfferedServiceEditorForm(this);
    private Button addServiceButton = new Button("Add Service");
    private Garage garage;
    private Duration duration;

    /**
     * The constructor for the sandbox view. Does initial layout setup, grid configuration, and event listener attachment
     *
     * @param serviceCatalogService the ServiceCatalogService to broker the repository calls for garage employees
     * @param garageService         the GarageService to broker the repository calls for garages
     */
    public ServicesSandboxView(ServiceCatalogService serviceCatalogService, GarageService garageService) {
        //initial layout setup
        this.serviceCatalogService = serviceCatalogService;
        this.garageService = garageService;
        addClassName("list-view");
        setSizeFull();

        //configure the service-catalog-grid
        grid.addClassName("service-catalog-grid");
        grid.setHeightByRows(true);
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

        grid.addColumn(offeredService -> {
            duration = offeredService.getDuration();
            return duration.toMinutes();
        }).setHeader("Duration in Minutes").setSortable(true).setKey("duration");

        //add garage
        grid.addColumn(offeredService -> {
            garage = offeredService.getServiceCategory().getGarage();
            return garage.getCompanyName();
        }).setSortable(true).setHeader("Garage");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // attach event listener on grid item select
        grid.asSingleSelect().addValueChangeListener(event -> editOfferedService(event.getValue()));

        // connect the handlers to the form events
        serviceEditorForm.addListener(OfferedServiceEditorForm.SaveEvent.class, this::saveOfferedService);
        serviceEditorForm.addListener(OfferedServiceEditorForm.DeleteEvent.class, this::deleteOfferedService);
        serviceEditorForm.addListener(OfferedServiceEditorForm.CloseEvent.class, this::closeOfferedServiceFormHandler);

        // set a click lister to the add button to show the service form and then hide the add button
        addServiceButton.addClickListener(event -> {
            serviceEditorForm.setVisible(true);
            addServiceButton.setVisible(false);
        });

        // set the forms' default visibility to false
        serviceEditorForm.setVisible(false);

        // build a div element with title, serviceCatalog grid, and editor forms
        Div offeredServiceContent = new Div(
                grid,
                serviceEditorForm
        );

        offeredServiceContent.addClassName("offeredServiceContent");
        offeredServiceContent.setSizeFull();

        // add the components to this layout
        add(new H1("Services"), addServiceButton, offeredServiceContent);

        // fetch the list for the grid
        updateOfferedServicesList();

        // pass down garages to the form for the garage combobox
        updateGarageCombobox();
    }

    public void setGarage(Garage garage){
        this.garage = garage;
        // pass down categories to the form for the categories combobox
        updateCategoriesCombobox();
    }

    private void updateGarageCombobox() {
        serviceEditorForm.setGarages(garageService.findAll());
    }

    private void updateCategoriesCombobox() {
        serviceEditorForm.setServiceCategories(serviceCatalogService.findCategoriesByGarage(garage));
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateOfferedServicesList() {
        grid.setItems(serviceCatalogService.findAllOfferedServices());
    }

    /**
     * Saves the given offeredService to the database
     *
     * @param event the SaveEvent from the offeredService editor form
     */
    private void saveOfferedService(OfferedServiceEditorForm.SaveEvent event) {
        serviceCatalogService.saveOfferedService(event.getOfferedService());
        updateOfferedServicesList();
        closeOfferedServiceEditorForm();
        // TODO add toast to confirm add
    }

    /**
     * Deletes the given offeredService from the database.
     *
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
     * Toggles the form visibility and initializes form fields if passed a OfferedService instance
     *
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
     * Clears and hides the editor form
     */
    private void closeOfferedServiceEditorForm() {
        serviceEditorForm.clearOfferedService(new OfferedService());
        serviceEditorForm.setVisible(false);
        removeClassName("editing-Service");
        addServiceButton.setVisible(true);
    }


    /**
     * Clears and hides the editor form
     */
    private void closeOfferedServiceFormHandler(OfferedServiceEditorForm.CloseEvent event) {
        if (event.getOfferedService() != null) {
            // TODO add confirm dialog
        }
        closeOfferedServiceEditorForm();
    }

}