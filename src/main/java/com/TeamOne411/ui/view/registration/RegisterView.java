package com.TeamOne411.ui.view.registration;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "register")
public class RegisterView extends VerticalLayout {
    private RegisterForm registerForm = new RegisterForm();

    public RegisterView() {
        addClassName("register-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H1("Garage Guide - New User Registration"), registerForm);
    }
}
