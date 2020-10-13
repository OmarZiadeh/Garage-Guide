package com.TeamOne411.ui.garage;

import com.TeamOne411.security.SecurityUtils;
import com.TeamOne411.ui.view.PlaceholderHomeView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.access.annotation.Secured;

/**
 * TODO: docs
 */
@Route("garage")
@Secured("ROLE_GARAGE_EMPLOYEE")
public class GarageEmployeeLayout extends AppLayout {
//    private User currentUser;

    public GarageEmployeeLayout() {
        createHeader();
        createDrawer();
//
//        GGUserDetails userDetails = (GGUserDetails) auth.getDetails();
//        currentUser = userDetails.getUser();
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Home", PlaceholderHomeView.class),
                new H5("Appointments (todo)")
        ));
    }

    private void createHeader() {
        Image logoImg = new Image("img/garagguidelogo.png", "logo");
        logoImg.setHeight("100px");
        logoImg.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(logoImg);

        if (SecurityUtils.isUserLoggedIn()) {
            header.add(
//                new H5("Hello, " + currentUser.getFirstName()),
                new Anchor("logout", "Log out")
            );
        } else {
            header.add(new Anchor("login", "Log In"));
        }

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        header.setPadding(true);

        addToNavbar(header);
    }
}
