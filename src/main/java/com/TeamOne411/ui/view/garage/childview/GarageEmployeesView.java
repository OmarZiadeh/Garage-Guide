package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.GarageEmployeeService;
import com.TeamOne411.backend.service.UserDetailsService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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

        Button deleteEmployeeButton = new Button("Delete Employee");
        deleteEmployeeButton.addClickListener(e -> deleteEmployeeClick());
        deleteEmployeeButton.setEnabled(false);

        grid.addSelectionListener(e -> {
            editEmployeeButton.setEnabled(!grid.getSelectedItems().isEmpty());
            deleteEmployeeButton.setEnabled(!grid.getSelectedItems().isEmpty());
        });

        add(
            new HorizontalLayout(registerEmployeeButton, editEmployeeButton, deleteEmployeeButton),
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
     * Fired on deleteEmployeeButton click. Shows a confirm dialog and then deletes the selected employee.
     */
    private void deleteEmployeeClick() {
        Optional<GarageEmployee> selectedEmployee = grid.getSelectedItems().stream().findFirst();

        if (selectedEmployee.isPresent()) {
            String message = "Are you sure you want to delete " + selectedEmployee.get().getFullName() + "?";

            ConfirmDialog confirmDeleteDialog = new ConfirmDialog(
                    "Delete Employee", message,
                    "Delete",
                    e -> onDeleteConfirm(selectedEmployee.get()),
                    "Cancel",
                    e -> e.getSource().close());

            confirmDeleteDialog.setConfirmButtonTheme("error primary");

            confirmDeleteDialog.open();
        }
    }

    /**
     * Fired when delete confirm dialog is confirmed by user. Deletes garage employee.
     * @param employee The employee to delete.
     */
    private void onDeleteConfirm(GarageEmployee employee) {
        if (employee != null) {
            garageEmployeeService.delete(employee);
            employeeUpdates("Deleted employee " + employee.getFullName());
        }
    }

    /**
     * Fired when a new employee is successfully registered using the employee editor dialog.
     * @param event the event that fired this method
     */
    private void onEmployeeRegisteredSuccess(ComponentEvent<EmployeeEditorDialog> event) {
        GarageEmployee newEmployee = event.getSource().getEmployee();
        employeeEditorDialog.close();

        if (newEmployee != null) {
            employeeUpdates("Successfully added new employee " + newEmployee.getFullName());
        }
    }

    /**
     * Fired when an existing employee is successfully edited using the employee editor dialog.
     * @param event the event that fired this method
     */
    private void onEmployeeEditedSuccess(ComponentEvent<EmployeeEditorDialog> event) {
        GarageEmployee editedEmployee = event.getSource().getEmployee();
        employeeEditorDialog.close();

        if (editedEmployee != null) {
            employeeUpdates("Successfully edited employee " + editedEmployee.getFullName());
        }
    }

    /**
     * This method includes some common functionality when any change to employees occurs
     * @param successMessage the message text to display to the user in a notification
     */
    private void employeeUpdates(String successMessage) {
        updateGarageEmployeeList();

        Notification notification = new Notification(
                successMessage,
                4000,
                Notification.Position.TOP_END
        );

        notification.open();
    }
}
