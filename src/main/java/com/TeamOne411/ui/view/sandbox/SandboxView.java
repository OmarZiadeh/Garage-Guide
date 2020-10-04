package com.TeamOne411.ui.view.sandbox;

import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Garage Guide - Sandbox")
public class SandboxView extends VerticalLayout {
    public SandboxView(GarageService garageService) {
        // add the sandbox views
        add(
                new GarageSandboxView(garageService)
        );
    }
}
