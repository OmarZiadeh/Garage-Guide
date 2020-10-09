package com.TeamOne411.ui.view.registration.subform;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GarageEmployeeConfirmationView extends VerticalLayout {
    public GarageEmployeeConfirmationView() {
        // initial view setup
        addClassName("garage-confirm-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(
                new H3("All good?"),
                new H5("Please verify your details, go back if there are any mistakes.")
        );
    }
}
