package com.TeamOne411.ui;

import com.TeamOne411.ui.view.PlaceholderHomeView;
import com.TeamOne411.ui.view.login.LoginView;
import com.TeamOne411.ui.view.sandbox.SandboxView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

/**
 * TODO: docs
 */
@PWA(name = "GarageGuide", shortName = "GarageGuide", enableInstallPrompt = false)
public class MainLayout extends AppLayout {
    public MainLayout() {
        createDrawer();
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Home", PlaceholderHomeView.class),
                new RouterLink("Sandbox", SandboxView.class),
                new RouterLink("Login", LoginView.class)
        ));
    }
}
