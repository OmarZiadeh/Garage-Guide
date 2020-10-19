package com.TeamOne411.ui.garage;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GGUserDetails;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.ui.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

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
    }
}
