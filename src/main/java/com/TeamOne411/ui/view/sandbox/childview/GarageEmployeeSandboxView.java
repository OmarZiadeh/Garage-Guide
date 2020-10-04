package com.TeamOne411.ui.view.sandbox.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.GarageEmployeeService;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.ui.view.sandbox.form.GarageEmployeeEditorForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * The GarageEmployeeSandboxView is a God-mode GarageEmployee editor for testing purposes.
 * This should either be deleted as the application evolves or turned into an Admin screen meant only for technical support, well hidden behind authentication.
 */
public class GarageEmployeeSandboxView extends VerticalLayout {
    private Grid<GarageEmployee> grid = new Grid<>(GarageEmployee.class);
    private GarageEmployeeService employeeService;
    private GarageService garageService;
    private GarageEmployeeEditorForm form = new GarageEmployeeEditorForm();
    private Button addButton = new Button("Add GarageEmployee");

    /**
     * The constructor for the sandbox view. Does initial layout setup, grid configuration, and event listener attachment
     * @param employeeService the GarageEmployeeService to broker the repository calls for garage employees
     * @param garageService the GarageService to broker the repository calls for garages
     */
    public GarageEmployeeSandboxView(GarageEmployeeService employeeService, GarageService garageService) {
        // initial layout setup
        this.employeeService = employeeService;
        this.garageService = garageService;
        addClassName("list-view");
        setSizeFull();

        // configure the garageEmployee grid
        grid.addClassName("garage-employee-grid");
        grid.setHeightByRows(true);
        grid.setMaxHeight("25vh");
        grid.setColumns("userName", "firstName", "lastName");
        grid.addColumn(garageEmployee -> {
            Garage garage = garageEmployee.getGarage();
            return garage == null ? "[None]" : garage.getCompanyName();
        }).setHeader("Employer");
        grid.addColumn("isAdmin");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // attach event listener on grid item select
        grid.asSingleSelect().addValueChangeListener(event -> editGarageEmployee(event.getValue()));

        // connect the handlers to the form events
        form.addListener(GarageEmployeeEditorForm.SaveEvent.class, this::saveGarageEmployee);
        form.addListener(GarageEmployeeEditorForm.DeleteEvent.class, this::deleteGarageEmployee);
        form.addListener(GarageEmployeeEditorForm.CloseEvent.class, this::closeGarageEmployeeFormHandler);

        // set a click lister to the add button to show the form and then hide the add button
        addButton.addClickListener(event -> {
            form.setVisible(true);
            addButton.setVisible(false);
        });

        // set the form's default visibility to false
        form.setVisible(false);

        // build a div element with title, garageEmployee grid, and editor form
        Div garageEmployeeContent = new Div(
            grid,
            form
        );

        garageEmployeeContent.addClassName("garageEmployeeContent");
        garageEmployeeContent.setSizeFull();

        // add the components to this layout
        add(new H1("Garage Employees"), addButton, garageEmployeeContent);

        // fetch the list for the grid
        updateGarageEmployeeList();

        // pass down garages to the form for the garage combobox
        updateGarageCombobox();
    }

    private void updateGarageCombobox() {
        form.setGarages(garageService.findAll());
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateGarageEmployeeList() {
        grid.setItems(employeeService.findAll());
    }

    /**
     * Saves the given garageEmployee to the database
     * @param event the SaveEvent from the garageEmployee editor form
     */
    private void saveGarageEmployee(GarageEmployeeEditorForm.SaveEvent event) {
        employeeService.save(event.getGarageEmployee());
        updateGarageEmployeeList();
        closeGarageEmployeeForm();
        // TODO add toast to confirm add
    }

    /**
     * Deletes the given garageEmployee from the database.
     * @param event the DeleteEvent from the garageEmployee editor form
     */
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
     */
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
     */
    private void closeGarageEmployeeForm() {
        form.setGarageEmployee(new GarageEmployee());
        form.setVisible(false);
        removeClassName("editing-garageEmployee");
        addButton.setVisible(true);
    }

    /**
     * Clears and hides the editor form
     */
    private void closeGarageEmployeeFormHandler(GarageEmployeeEditorForm.CloseEvent event) {
        if (event.getGarageEmployee() != null)
        {
            // TODO add confirm dialog
        }
        closeGarageEmployeeForm();
    }
}
