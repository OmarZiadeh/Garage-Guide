package com.TeamOne411.ui.view.garage;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GGUserDetails;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.GarageEmployeeService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.ui.MainLayout;
import com.TeamOne411.ui.view.garage.childview.GarageAppointmentsView;
import com.TeamOne411.ui.view.garage.childview.GarageBusinessHoursView;
import com.TeamOne411.ui.view.garage.childview.GarageEmployeesView;
import com.TeamOne411.ui.view.garage.childview.GarageServicesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: docs
 */
@Route(value = "garage", layout = MainLayout.class)
@Secured("ROLE_GARAGE_EMPLOYEE")
@PageTitle("My Garage")
public class GarageEmployeeHomeView extends VerticalLayout {
    private GGUserDetails userDetails;
    private GarageEmployee loggedInEmployee;
    private Garage myGarage;

    public GarageEmployeeHomeView(UserDetailsService userDetailsService, GarageEmployeeService garageEmployeeService,
                                  ServiceCatalogService serviceCatalogService) {
        userDetails = userDetailsService.getLoggedInUserDetails();
        loggedInEmployee = (GarageEmployee) userDetails.getUser();
        myGarage = loggedInEmployee.getGarage();
        add(new H2("My Garage - " + myGarage.getCompanyName()));

        Tabs tabs = new Tabs();
        Div pages = new Div();
        Map<Tab, Component> tabsToPages = new HashMap<>();

        /*
        First Tab - Appointments
         */
        Tab appointmentsTab = new Tab("Appointments");
        GarageAppointmentsView appointmentsView = new GarageAppointmentsView();
        Div appointmentsPage = new Div(appointmentsView);
        appointmentsTab.add(appointmentsPage);
        tabs.add(appointmentsTab);
        pages.add(appointmentsPage);
        tabsToPages.put(appointmentsTab, appointmentsPage);

        if (loggedInEmployee.getIsAdmin()) {
            /*
            Second Tab - Garage Employees
             */
            Tab employeesTab = new Tab("Employee Management");
            GarageEmployeesView employeesView = new GarageEmployeesView(garageEmployeeService, userDetailsService, myGarage);
            Div employeesPage = new Div(employeesView);
            employeesTab.add(employeesPage);
            tabs.add(employeesTab);
            pages.add(employeesPage);
            employeesPage.setVisible(false);
            tabsToPages.put(employeesTab, employeesPage);

            /*
            Third Tab - Garage Services
             */
            Tab servicesTab = new Tab("Services");
            GarageServicesView servicesView = new GarageServicesView(serviceCatalogService, myGarage);
            Div servicesPage = new Div(servicesView);
            servicesTab.add(servicesPage);
            tabs.add(servicesTab);
            pages.add(servicesPage);
            servicesPage.setVisible(false);
            tabsToPages.put(servicesTab, servicesPage);

            /*
            Fourth Tab - Garage Business Hours (aka Schedule)
             */
            Tab businessHoursTab = new Tab("Business Hours");
            GarageBusinessHoursView businessHoursView = new GarageBusinessHoursView();
            Div businessHoursPage = new Div(businessHoursView);
            businessHoursTab.add(businessHoursPage);
            tabs.add(businessHoursTab);
            pages.add(businessHoursPage);
            businessHoursPage.setVisible(false);
            tabsToPages.put(businessHoursTab, businessHoursPage);
        }

        // todo add more tabs here

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
