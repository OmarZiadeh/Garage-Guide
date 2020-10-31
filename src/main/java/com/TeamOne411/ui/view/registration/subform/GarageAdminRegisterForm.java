package com.TeamOne411.ui.view.registration.subform;

import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.service.UserDetailsService;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;

public class GarageAdminRegisterForm extends GarageEmployeeRegisterForm {

    public GarageAdminRegisterForm(UserDetailsService userDetailsService) {
        super(userDetailsService);
        // have to add these to first in reverse order so the second one goes to the top
        addComponentAsFirst(new H5("We'll ask for details about your garage on the next page"));
        addComponentAsFirst(new H3("Let's start with your information."));

        setBackButtonText("Back To User Selection");
        setNextButtonText("Enter Garage Info");

        // Set the isAdmin to true (always true for this subclass) and prevent user from doing something stupid.
        setIsAdminToggleValue(true);
        setIsAdminToggleEnabled(false);
    }

    @Override
    public GarageEmployee getValidGarageEmployee() {
        GarageEmployee employee = super.getValidGarageEmployee();

        if (employee != null)
            employee.setIsAdmin(true);

        return employee;
    }
}
