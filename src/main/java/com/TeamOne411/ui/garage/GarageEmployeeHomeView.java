package com.TeamOne411.ui.garage;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GGUserDetails;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.ui.MainLayout;
import com.TeamOne411.ui.garage.childview.GarageAppointmentsView;
import com.TeamOne411.ui.garage.childview.GarageEmployeesView;
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

    public GarageEmployeeHomeView(UserDetailsService userDetailsService) {
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

        /*
        Second Tab - Garage Employees
         */
        // todo make this only visible to admins
        Tab employeesTab = new Tab("Employee Management");
        GarageEmployeesView employeesView = new GarageEmployeesView();
        Div employeesPage = new Div(employeesView);
        employeesTab.add(employeesPage);
        tabs.add(employeesTab);
        pages.add(employeesPage);
        employeesPage.setVisible(false);
        tabsToPages.put(employeesTab, employeesPage);

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
