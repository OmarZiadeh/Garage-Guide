package com.TeamOne411.backend.entity.users;

import com.TeamOne411.backend.entity.Garage;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;
import java.util.List;

@Entity
public class CarOwner extends User {
    @NotNull
    @NotEmpty(message = "Phone number can't be empty.")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone number must be in the format: 555-555-5555")
    private String phoneNumber = "";

    @NotNull
    @NotEmpty(message = "Address can't be empty.")
    private String address = "";



    @ManyToMany()
    private List<Garage> preferredGarages = new LinkedList<>();

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Garage> getPreferredGarages() {
        return preferredGarages;
    }

    public void setPreferredGarages(List<Garage> preferredGarages) {
        this.preferredGarages = preferredGarages;
    }
}
