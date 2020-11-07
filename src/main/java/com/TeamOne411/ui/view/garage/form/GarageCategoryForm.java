package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

public class GarageCategoryForm extends VerticalLayout {

    private TextField categoryName = new TextField("Category name");

    Binder<ServiceCategory> binder = new BeanValidationBinder<>(ServiceCategory.class);
    private ServiceCategory serviceCategory = new ServiceCategory();
    private Garage garage;

    public GarageCategoryForm(Garage garage) {
        this.garage = garage;

        addClassName("garage-category-form");
        binder.bindInstanceFields(this);
        add(categoryName);
    }
}
