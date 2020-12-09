package com.TeamOne411.backend.entity;

import com.TeamOne411.backend.entity.users.CarOwner;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Vehicle extends AbstractEntity {

    //Make Model and Year might change based off of how it is selected in form
    @NotNull
    @NotEmpty(message = "Make can't be empty.")
    private String make = "";

    @NotNull
    @NotEmpty(message = "Model can't be empty.")
    private String model = "";

    @NotNull
    @NotEmpty(message = "Year can't be empty.")
    @Pattern(regexp = "^\\d{4}$", message = "Invalid year, must be from 1980 to current year")
    private String year = "";

    @NotNull
    @NotEmpty(message = "VIN can't be empty.")
    @Pattern(regexp = "^[A-HJ-NPR-Za-hj-npr-z\\d]{8}[\\dX][A-HJ-NPR-Za-hj-npr-z\\d]{2}\\d{6}$", message = "Invalid VIN, must be 17 digits with correct format")
    private String vin = "";

    @NotNull
    @ManyToOne
    @JoinColumn(name = "car_owner_id")
    private CarOwner carOwner;


    public CarOwner getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(CarOwner carOwner){
        this.carOwner = carOwner;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getName(){
        String name = getYear() + " " + getModel() + " " + getMake();
        return name;
    }

}
