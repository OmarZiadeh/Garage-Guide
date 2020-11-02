package com.TeamOne411.backend.entity;

import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.sun.org.apache.xpath.internal.operations.Mod;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Car extends AbstractEntity {

    //Make Model and Year might change based off of how it is selected in form
    @NotNull
    @NotEmpty(message = "Make can't be empty.")
    private String Make = "";

    @NotNull
    @NotEmpty(message = "Model can't be empty.")
    private String Model = "";

    @NotNull
    @NotEmpty(message = "Year can't be empty.")
    private String Year = "";

    @NotNull
    @NotEmpty(message = "VIN can't be empty.")
    @Pattern(regexp = "^\\d{1}\\s{4}\\d{2}\\s{4}\\d{6}$", message = "Invalid VIN, must be 17 digits with correct format")
    private String VIN = "";


    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String Make) {
        this.Make = Make;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

}
