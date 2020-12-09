package com.TeamOne411.ui.view.carowner;

import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.entity.users.GGUserDetails;
import com.TeamOne411.backend.service.*;
import com.TeamOne411.backend.service.api.car.ApiVehicleService;
import com.TeamOne411.ui.MainLayout;
import com.TeamOne411.ui.view.carowner.childview.CarOwnerAppointmentsView;
import com.TeamOne411.ui.view.carowner.childview.CarOwnerServiceHistoryView;
import com.TeamOne411.ui.view.carowner.childview.CarOwnerVehiclesView;
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
@Route(value = "carOwner", layout = MainLayout.class)
@Secured("ROLE_CAR_OWNER")
@PageTitle("Garage Guide")
public class CarOwnerHomeView extends VerticalLayout {
    private GGUserDetails userDetails;
    private CarOwner loggedInCarOwner;
    private ApiVehicleService apiVehicleService;

    public CarOwnerHomeView(UserDetailsService userDetailsService,
                            VehicleService vehicleService,
                            ApiVehicleService apiVehicleService,
                            AppointmentService appointmentService,
                            ServiceCatalogService serviceCatalogService,
                            GarageCalendarService garageCalendarService) {
        userDetails = userDetailsService.getLoggedInUserDetails();
        loggedInCarOwner = (CarOwner) userDetails.getUser();
        this.apiVehicleService = apiVehicleService;
        add(new H2("Welcome back " + loggedInCarOwner.getFirstName()));

        Tabs tabs = new Tabs();
        Div pages = new Div();
        Map<Tab, Component> tabsToPages = new HashMap<>();

        /*
        First Tab - Appointments
         */
        Tab appointmentsTab = new Tab("Appointments");
        CarOwnerAppointmentsView appointmentsView = new CarOwnerAppointmentsView(appointmentService,
                serviceCatalogService, garageCalendarService, vehicleService, loggedInCarOwner);
        Div appointmentsPage = new Div(appointmentsView);
        appointmentsTab.add(appointmentsPage);
        tabs.add(appointmentsTab);
        pages.add(appointmentsPage);
        tabsToPages.put(appointmentsTab, appointmentsPage);

        /*
        Second Tab - Vehicles
         */
        Tab vehiclesTab = new Tab("Vehicles");
        CarOwnerVehiclesView vehicleView = new CarOwnerVehiclesView(vehicleService, apiVehicleService, loggedInCarOwner);
        Div vehiclesPage = new Div(vehicleView);
        vehiclesTab.add(vehiclesPage);
        tabs.add(vehiclesTab);
        pages.add(vehiclesPage);
        tabsToPages.put(vehiclesTab, vehiclesPage);
        vehiclesPage.setVisible(false);

        /*
        Third Tab - Service History
         */
        Tab serviceHistoryTab = new Tab("Service History");
        CarOwnerServiceHistoryView serviceHistoryView = new CarOwnerServiceHistoryView();
        Div serviceHistoryPage = new Div(serviceHistoryView);
        serviceHistoryTab.add(serviceHistoryPage);
        tabs.add(serviceHistoryTab);
        pages.add(serviceHistoryPage);
        tabsToPages.put(serviceHistoryTab, serviceHistoryPage);
        serviceHistoryPage.setVisible(false);

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
