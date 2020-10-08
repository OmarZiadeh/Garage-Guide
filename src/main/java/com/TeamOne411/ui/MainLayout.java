package com.TeamOne411.ui;

import com.TeamOne411.security.SecurityUtils;
import com.TeamOne411.ui.view.PlaceholderHomeView;
import com.TeamOne411.ui.view.sandbox.SandboxView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

/**
 * TODO: docs
 */
@PWA(name = "GarageGuide", shortName = "GarageGuide", enableInstallPrompt = false)
public class MainLayout extends AppLayout {
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Home", PlaceholderHomeView.class),
                new RouterLink("Sandbox", SandboxView.class)
        ));
    }

    private void createHeader() {
        H1 logo = new H1("Garage Guide");
        logo.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(logo);

        if (SecurityUtils.isUserLoggedIn()) {
            header.add(new Anchor("logout", "Log out"));
        } else {
            header.add(new Anchor("login", "Log In"));
        }

        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        header.setPadding(true);

        addToNavbar(header);
    }
}
