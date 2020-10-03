package com.TeamOne411.ui;

import com.TeamOne411.ui.view.testing.SandboxView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.router.RouterLink;

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
                new RouterLink("Sandbox", SandboxView.class)
        ));
    }
}
