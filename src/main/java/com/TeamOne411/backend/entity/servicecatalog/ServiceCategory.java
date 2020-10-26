package com.TeamOne411.backend.entity.servicecatalog;

import com.TeamOne411.backend.entity.AbstractEntity;

import javax.validation.constraints.Size;

public class ServiceCategory extends AbstractEntity{

    @Size(min = 2, message = "Category name must be at least two characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    TODO: Need to determine if this method is needed or not.
     Why do we need this method 'toString' when there is already the getName() method up above?
     */
    @Override
    public String toString() {
        return getName();
    }
}