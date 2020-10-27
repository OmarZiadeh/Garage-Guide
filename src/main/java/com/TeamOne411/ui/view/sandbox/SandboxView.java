package com.TeamOne411.ui.view.sandbox;

import com.TeamOne411.backend.service.CarOwnerService;
import com.TeamOne411.backend.service.GarageEmployeeService;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.MainLayout;
import com.TeamOne411.ui.view.sandbox.childview.CarOwnerSandboxView;
import com.TeamOne411.ui.view.sandbox.childview.GarageEmployeeSandboxView;
import com.TeamOne411.ui.view.sandbox.childview.GarageSandboxView;
import com.TeamOne411.ui.view.sandbox.childview.ServiceCatalogSandboxView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.HashMap;
import java.util.Map;

@Route(value = "sandbox", layout = MainLayout.class)
@Secured("ROLE_GG_ADMIN")
@PageTitle("Garage Guide - Sandbox")
public class SandboxView extends VerticalLayout {
    public SandboxView(GarageService garageService,
                       GarageEmployeeService garageEmployeeService,
                       CarOwnerService carOwnerService,
                        ServiceCatalogService serviceCatalogService) {
        Tabs tabs = new Tabs();
        Div pages = new Div();
        Map<Tab, Component> tabsToPages = new HashMap<>();

        /*
        First Tab - Garages
         */
        Tab garagesTab = new Tab("Garages");
        GarageSandboxView garageSandboxView = new GarageSandboxView(garageService);
        Div garagesPage = new Div(garageSandboxView);
        garagesPage.setSizeFull();
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
        employeesPage.setSizeFull();
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
        carOwnersPage.setSizeFull();
        tabs.add(carOwnersTab);
        pages.add(carOwnersPage);
        carOwnersPage.setVisible(false);
        tabsToPages.put(carOwnersTab, carOwnersPage);

        //TODO this is a temporary UI for testing. Needs to be rewritten or removed later
        /*
        Forth Tab - Car Owners
         */
        Tab serviceCatalogTab = new Tab("Services");
        ServiceCatalogSandboxView serviceCatalogSandboxView = new ServiceCatalogSandboxView(serviceCatalogService, garageService);
        Div serviceCatalogPage = new Div(serviceCatalogSandboxView);
        serviceCatalogPage.setSizeFull();
        tabs.add(serviceCatalogTab);
        pages.add(serviceCatalogPage);
        serviceCatalogPage.setVisible(false);
        tabsToPages.put(serviceCatalogTab, serviceCatalogPage);

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
