package com.TeamOne411.backend.entity.servicecatalog;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class ServiceCategory extends AbstractEntity {
    @Size(min = 2, message = "Category name must be at least two characters")
    private String name;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }
}