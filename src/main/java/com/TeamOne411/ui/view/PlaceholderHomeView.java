package com.TeamOne411.ui.view;

import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.entity.users.User;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.security.SecurityUtils;
import com.TeamOne411.ui.MainLayout;
import com.TeamOne411.ui.view.garage.GarageEmployeeHomeView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class PlaceholderHomeView extends VerticalLayout implements BeforeEnterObserver {
    private UserDetailsService userDetailsService;

    public PlaceholderHomeView(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER);

        add(
                new H1("Welcome to GarageGuide! Sign in or register a new account."),
                new Text("This is just a placeholder until the landing page is complete.")
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (SecurityUtils.isUserLoggedIn()) {
            User loggedInUser = userDetailsService.getLoggedInUserDetails().getUser();

            // reroute logged in user to their relevant home view
            if (loggedInUser instanceof GarageEmployee) {
                beforeEnterEvent.rerouteTo(GarageEmployeeHomeView.class);
            } else if (loggedInUser instanceof CarOwner) {
                // todo placeholder for reroute
            }
        }
    }
}
