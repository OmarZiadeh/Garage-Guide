package com.TeamOne411.backend.entity.users;

import com.TeamOne411.backend.entity.Garage;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
public class CarOwner extends UserAccount {
    @NotNull
    @NotEmpty
    private String phoneNumber = "";

    @NotNull
    @NotEmpty
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
