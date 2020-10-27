package com.TeamOne411.backend.entity.servicecatalog;

import com.TeamOne411.backend.entity.AbstractEntity;

import javax.validation.constraints.Size;

public class ServiceCategory extends AbstractEntity{

    @Size(min = 2, message = "Category name must be at least two characters")
    private String categoryName;

    public String getName() {
        return categoryName;
    }

    public void setName(String name) {
        this.categoryName = name;
    }
}
