package com.TeamOne411.ui.view.testing;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Garage Guide - Sandbox")
public class SandboxView extends VerticalLayout {
    Grid<Garage> garageGrid = new Grid<>(Garage.class);
    private GarageService garageService;

    public SandboxView(GarageService garageService) {
        this.garageService = garageService;
        addClassName("list-view");
        setSizeFull();
        configureGarageGrid();

        Div garageContent = new Div(
            new H1("Garages"),
            garageGrid,
            new GarageForm()
        );

        garageContent.addClassName("garageContent");
        add(garageContent);
        updateGarageList();
    }
    private void configureGarageGrid() {
        garageGrid.addClassName("garage-grid");
        garageGrid.setSizeFull();
        garageGrid.setColumns("companyName", "phoneNumber", "address");

        garageGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }
    private void updateGarageList() {
        garageGrid.setItems(garageService.findAll());
    }
}
