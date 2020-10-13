package com.TeamOne411.ui.view;

import com.TeamOne411.ui.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class PlaceholderHomeView extends VerticalLayout {
    public PlaceholderHomeView() {
        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER);

        add(
                new H1("Welcome to GarageGuide! Click a link on the left"),
                new Text("This is just a placeholder until the landing page is complete.")
        );
    }
}
