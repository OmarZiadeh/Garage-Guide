package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.GarageEmployeeService;
import com.TeamOne411.backend.service.UserDetailsService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Optional;

/**
 * This class is a Vertical layout that shows a list of employees for a given garage and controls to add new or edit them.
 */
public class GarageEmployeesView extends VerticalLayout {
    GarageEmployeeService garageEmployeeService;

    private Grid<GarageEmployee> grid = new Grid<>(GarageEmployee.class);
    private UserDetailsService userDetailsService;
    private Garage employer;
    private EmployeeEditorDialog employeeEditorDialog;

    public GarageEmployeesView(
            GarageEmployeeService garageEmployeeService,
            UserDetailsService userDetailsService,
            Garage employer
    ) {
        this.garageEmployeeService = garageEmployeeService;
        this.userDetailsService = userDetailsService;
        this.employer = employer;

        // configure the garageEmployee grid
        grid.addClassName("garage-employee-grid");
        grid.setHeightByRows(true);
        grid.setMaxHeight("25vh");
        grid.setColumns("username", "firstName", "lastName", "email", "isAdmin");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        Button registerEmployeeButton = new Button("Register New Employee");
        registerEmployeeButton.addClickListener(e -> showNewEmployeeDialog());

        Button editEmployeeButton = new Button("Edit Employee");
        editEmployeeButton.addClickListener(e -> showEditEmployeeDialog());
        editEmployeeButton.setEnabled(false);

        grid.addSelectionListener(e -> {
            if (!grid.getSelectedItems().isEmpty()) {
                editEmployeeButton.setEnabled(true);
            } else {
                editEmployeeButton.setEnabled(false);
            }
        });

        add(
            new HorizontalLayout(registerEmployeeButton, editEmployeeButton),
            grid
        );

        updateGarageEmployeeList();
    }

    /**
     * Calls the GarageEmployeeService to refresh the list of employees. Call this anytime the employees may have been edited.
     */
    private void updateGarageEmployeeList() {
        grid.setItems(garageEmployeeService.findByGarage(employer));
    }

    /**
     * Creates and opens a new EmployeeEditorDialog instance not in edit mode (to register new employee)
     */
    private void showNewEmployeeDialog() {
        employeeEditorDialog = new EmployeeEditorDialog(userDetailsService, employer);

        employeeEditorDialog.setWidth("250px");
        employeeEditorDialog.setWidth("750px");

        employeeEditorDialog.addListener(EmployeeEditorDialog.AddEmployeeSuccessEvent.class, this::onEmployeeRegisteredSuccess);

        employeeEditorDialog.open();
    }

    /**
     * Gets the selected employee from the grid, passes it to a new EmployeeEditorDialog instance and opens it in Edit Mode.
     */
    private void showEditEmployeeDialog() {
        Optional<GarageEmployee> selectedEmployee = grid.getSelectedItems().stream().findFirst();

        if (selectedEmployee.isPresent()) {
            employeeEditorDialog = new EmployeeEditorDialog(userDetailsService, selectedEmployee.get());

            employeeEditorDialog.setWidth("250px");
            employeeEditorDialog.setWidth("750px");

            employeeEditorDialog.addListener(EmployeeEditorDialog.EditEmployeeSuccessEvent.class, this::onEmployeeEditedSuccess);

            employeeEditorDialog.open();
        }
    }

    /**
     * Fired when a new employee is successfully registered using the employee editor dialog.
     * @param event the event that fired this method
     */
    private void onEmployeeRegisteredSuccess(ComponentEvent<EmployeeEditorDialog> event) {
        GarageEmployee newEmployee = event.getSource().getEmployee();

        if (newEmployee != null) {
            editEmployeeDialogSuccess("Successfully added new employee " + newEmployee.getFirstName() + " " + newEmployee.getLastName());
        }
    }

    /**
     * Fired when an existing employee is successfully edited using the employee editor dialog.
     * @param event the event that fired this method
     */
    private void onEmployeeEditedSuccess(ComponentEvent<EmployeeEditorDialog> event) {
        GarageEmployee editedEmployee = event.getSource().getEmployee();

        if (editedEmployee != null) {
            editEmployeeDialogSuccess("Successfully edited employee " + editedEmployee.getFirstName() + " " + editedEmployee.getLastName());
        }
    }

    /**
     * This method includes some common functionality for the success events
     * @param successMessage the message text to display to the user in a notification
     */
    private void editEmployeeDialogSuccess(String successMessage) {
        employeeEditorDialog.close();
        updateGarageEmployeeList();

        Notification notification = new Notification(
                successMessage,
                4000,
                Notification.Position.TOP_END
        );

        notification.open();
    }
}
