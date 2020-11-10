package com.TeamOne411.ui.view;

import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.entity.users.User;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.security.SecurityUtils;
import com.TeamOne411.ui.MainLayout;
import com.TeamOne411.ui.view.garage.GarageEmployeeHomeView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class WelcomeView extends VerticalLayout implements BeforeEnterObserver {
    private UserDetailsService userDetailsService;

    public WelcomeView(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER);

        Button newuser_button = new Button("Create Account");
        newuser_button.addClickListener(e ->
                newuser_button.getUI().ifPresent(ui ->
                        ui.navigate("register")));

        Button login_button = new Button("Log In");
        login_button.addClickListener(e ->
                login_button.getUI().ifPresent(ui ->
                        ui.navigate("login")));

        HorizontalLayout buttons = new HorizontalLayout(newuser_button, login_button);

        add(
                new H1("Welcome to GarageGuide!"),
                new H2("Sign in or register a new account."),
                buttons
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
