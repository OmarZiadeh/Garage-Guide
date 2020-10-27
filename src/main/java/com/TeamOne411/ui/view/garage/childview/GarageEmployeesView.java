package com.TeamOne411.ui.view.garage.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.GarageEmployeeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GarageEmployeesView extends VerticalLayout {
    GarageEmployeeService garageEmployeeService;

    private Grid<GarageEmployee> grid = new Grid<>(GarageEmployee.class);
    private Garage employer;

    public GarageEmployeesView(GarageEmployeeService garageEmployeeService, Garage employer) {
        this.garageEmployeeService = garageEmployeeService;
        this.employer = employer;

        // configure the garageEmployee grid
        grid.addClassName("garage-employee-grid");
        grid.setHeightByRows(true);
        grid.setMaxHeight("25vh");
        grid.setColumns("username", "firstName", "lastName", "email", "isAdmin");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        Button registerEmployeeButton = new Button("Register New Employee");
        // todo hook up button to modal dialog or separate registration page

        add(
            new HorizontalLayout(registerEmployeeButton),
            grid
        );

        updateGarageEmployeeList();
    }

    private void updateGarageEmployeeList() {
        grid.setItems(garageEmployeeService.findByGarage(employer));
    }


}
