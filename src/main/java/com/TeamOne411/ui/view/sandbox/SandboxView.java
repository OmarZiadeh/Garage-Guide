package com.TeamOne411.ui.view.sandbox;

import com.TeamOne411.backend.service.CarOwnerService;
import com.TeamOne411.backend.service.GarageEmployeeService;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.ui.MainLayout;
import com.TeamOne411.ui.view.sandbox.childview.CarOwnerSandboxView;
import com.TeamOne411.ui.view.sandbox.childview.GarageEmployeeSandboxView;
import com.TeamOne411.ui.view.sandbox.childview.GarageSandboxView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;
import java.util.Map;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Garage Guide - Sandbox")
public class SandboxView extends VerticalLayout {
    public SandboxView(GarageService garageService,
                       GarageEmployeeService garageEmployeeService,
                       CarOwnerService carOwnerService) {
        Tabs tabs = new Tabs();
        Div pages = new Div();
        Map<Tab, Component> tabsToPages = new HashMap<>();

        /*
        First Tab - Garages
         */
        Tab garagesTab = new Tab("Garages");
        GarageSandboxView garageSandboxView = new GarageSandboxView(garageService);
        Div garagesPage = new Div(garageSandboxView);
        garagesTab.add(garagesPage);
        tabs.add(garagesTab);
        pages.add(garagesPage);
        tabsToPages.put(garagesTab, garagesPage);

        /*
        Second Tab - Garage Employees
         */
        Tab employeesTab = new Tab("Garage Employees");
        GarageEmployeeSandboxView employeeSandboxView = new GarageEmployeeSandboxView(garageEmployeeService, garageService);
        Div employeesPage = new Div(employeeSandboxView);
        employeesTab.add(employeesPage);
        tabs.add(employeesTab);
        pages.add(employeesPage);
        employeesPage.setVisible(false);
        tabsToPages.put(employeesTab, employeesPage);

        /*
        Third Tab - Car Owners
         */
        Tab carOwnersTab = new Tab("Car Owners");
        CarOwnerSandboxView carOwnerSandboxView = new CarOwnerSandboxView(carOwnerService);
        Div carOwnersPage = new Div(carOwnerSandboxView);
        tabs.add(carOwnersTab);
        pages.add(carOwnersPage);
        carOwnersPage.setVisible(false);
        tabsToPages.put(carOwnersTab, carOwnersPage);

        // hook up the listener for tab change
        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        // add the sandbox tabs and pages
        add(tabs, pages);
        pages.setSizeFull();
        setSizeFull();
    }
}
