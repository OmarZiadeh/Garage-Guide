package com.TeamOne411.ui.view.sandbox.childview;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.sandbox.form.ServiceCatalogEditorForm;
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
    private ServiceCatalogEditorForm form = new ServiceCatalogEditorForm();
    private Button addButton = new Button("Add Service");

    /**
     * The constructor for the sandbox view. Does initial layout setup, grid configuration, and event listener attachment
     * @param serviceCatalogService the ServiceCatalogService to broker the repository calls for OfferedServices
     */
    public ServiceCatalogSandboxView(ServiceCatalogService serviceCatalogService) {
        // initial layout setup
        this.serviceCatalogService = serviceCatalogService;
        addClassName("list-view");
        setSizeFull();

        // configure the ServiceCatalog grid
        grid.addClassName("service-catalog-grid");
        grid.setHeightByRows(true);
        grid.setMaxHeight("25vh");
        grid.setColumns("serviceName", "serviceDescription");
        grid.addColumn(OfferedService::getServiceCategory).setHeader("Category").setSortable(true).setKey("serviceCategory");

        // Format and add " $" to price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        grid.addColumn(offeredService -> decimalFormat.format(offeredService.getPrice()))
                .setHeader("Price")
                .setComparator(Comparator.comparing(OfferedService::getPrice)).setKey("price");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
/*
        // attach event listener on grid item select
        grid.asSingleSelect().addValueChangeListener(event -> editOfferedService(event.getValue()));

        // connect the handlers to the form events
        form.addListener(ServiceCatalogEditorForm.SaveEvent.class, this::saveOfferedService);
        form.addListener(GarageEmployeeEditorForm.DeleteEvent.class, this::deleteGarageEmployee);
        form.addListener(GarageEmployeeEditorForm.CloseEvent.class, this::closeGarageEmployeeFormHandler);

        // set a click lister to the add button to show the form and then hide the add button
        addButton.addClickListener(event -> {
            form.setVisible(true);
            addButton.setVisible(false);
        });


*/

        // set the form's default visibility to false
        form.setVisible(false);


        // build a div element with title, garageEmployee grid, and editor form
        Div serviceCatalogContent = new Div(
                grid//,
                //form
        );


        serviceCatalogContent.addClassName("serviceCatalogContent");
        serviceCatalogContent.setSizeFull();



        // add the components to this layout
        add(new H1("Services"), serviceCatalogContent); //addButton,


        // fetch the list for the grid
        updateServiceCatalogList();

        /*
            TODO REVISE THIS
        // pass down garages to the form for the garage combobox
         updateGarageCombobox();

         */
    }
    /*
    private void updateGarageCombobox() {
        form.setGarages(garageService.findAll());
    }
    */

    /**
     * Refreshes the grid list from the database
     */
    private void updateServiceCatalogList() {
        grid.setItems(serviceCatalogService.findAllOfferedServices());
    }

/*
    /**
     * Saves the given garageEmployee to the database
     * @param event the SaveEvent from the garageEmployee editor form

    private void saveGarageEmployee(GarageEmployeeEditorForm.SaveEvent event) {
        employeeService.save(event.getGarageEmployee());
        updateGarageEmployeeList();
        closeGarageEmployeeForm();
        // TODO add toast to confirm add
    }


    /**
     * Deletes the given garageEmployee from the database.
     * @param event the DeleteEvent from the garageEmployee editor form

    private void deleteGarageEmployee(GarageEmployeeEditorForm.DeleteEvent event) {
        if (event.getGarageEmployee() != null) {
            // TODO make this run only after confirm delete dialog
            employeeService.delete(event.getGarageEmployee());
            updateGarageEmployeeList();
        }
        closeGarageEmployeeForm();
    }

    /**
     * Toggles the form visibility and sets initializes form fields if passed a GarageEmployee instance
     * @param garageEmployee the GarageEmployee instance to edit, or null if none is selected

    private void editGarageEmployee(GarageEmployee garageEmployee) {
        if (garageEmployee == null) {
            closeGarageEmployeeForm();
        } else {
            form.setGarageEmployee(garageEmployee);
            form.setVisible(true);
            addClassName("editing-garageEmployee");
        }
    }

    /**
     * Clears and hides the editor form

    private void closeGarageEmployeeForm() {
        form.setGarageEmployee(new GarageEmployee());
        form.setVisible(false);
        removeClassName("editing-garageEmployee");
        addButton.setVisible(true);
    }

    /**
     * Clears and hides the editor form

    private void closeGarageEmployeeFormHandler(GarageEmployeeEditorForm.CloseEvent event) {
        if (event.getGarageEmployee() != null)
        {
            // TODO add confirm dialog
        }
        closeGarageEmployeeForm();
    }
    */
}