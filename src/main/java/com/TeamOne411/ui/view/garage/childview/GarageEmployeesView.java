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

public class GarageEmployeesView extends VerticalLayout {
    GarageEmployeeService garageEmployeeService;

    private Grid<GarageEmployee> grid = new Grid<>(GarageEmployee.class);
    private UserDetailsService userDetailsService;
    private Garage employer;
    private NewEmployeeDialog newEmployeeDialog;

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

        add(
            new HorizontalLayout(registerEmployeeButton),
            grid
        );

        updateGarageEmployeeList();
    }

    private void updateGarageEmployeeList() {
        grid.setItems(garageEmployeeService.findByGarage(employer));
    }

    private void showNewEmployeeDialog() {
        newEmployeeDialog = new NewEmployeeDialog(userDetailsService, employer);

        newEmployeeDialog.setWidth("250px");
        newEmployeeDialog.setWidth("750px");

        newEmployeeDialog.addListener(NewEmployeeDialog.SuccessEvent.class, this::onEmployeeRegisteredSuccess);

        newEmployeeDialog.open();
    }


    public void onEmployeeRegisteredSuccess(ComponentEvent<NewEmployeeDialog> event) {
        newEmployeeDialog.close();
        updateGarageEmployeeList();
        GarageEmployee newEmployee = event.getSource().getNewEmployee();

        if (newEmployee != null) {
            Notification notification = new Notification(
                    "Successfully added new employee " + newEmployee.getFirstName() + " " + newEmployee.getLastName(),
                    3000,
                    Notification.Position.TOP_END
            );

            notification.open();
        }
    }
}
