package com.TeamOne411.ui;

import com.TeamOne411.backend.entity.users.GGUserDetails;
import com.TeamOne411.backend.service.UserDetailsService;
import com.TeamOne411.security.SecurityUtils;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.PWA;

/**
 * TODO: docs
 */
@PWA(name = "GarageGuide", shortName = "GarageGuide", enableInstallPrompt = false)
public class MainLayout extends AppLayout {
    private GGUserDetails userDetails;
    public MainLayout(UserDetailsService userDetailsService) {
        userDetails = userDetailsService.getLoggedInUserDetails();
        createHeader();
    }

    private void createHeader() {
        Image logoImg = new Image("img/garagguidelogo.png", "logo");
        logoImg.setHeight("50px");
        logoImg.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(logoImg);

        if (SecurityUtils.isUserLoggedIn()) {
            header.add(new Anchor("logout", "Log out"));
        } else {
            header.add(new Anchor("register", "Create Account"));
            header.add(new Anchor("login", "Log In"));
        }

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        header.setPadding(true);

        addToNavbar(header);
    }
}
