package com.TeamOne411.backend.entity;

import com.TeamOne411.backend.entity.users.GarageEmployee;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Garage extends AbstractEntity {
    @NotNull
    @NotEmpty
    private String companyName = "";

//  TODO: add better validation
    @NotNull
    @NotEmpty
    private String phoneNumber = "";

//  TODO: add better validation
    @NotNull
    @NotEmpty
    private String address = "";

//  TODO: We'll probably want these at some point, but right now it'll break Garage creation
//    @NotNull
//    @NotEmpty
    @OneToMany(mappedBy = "garage", fetch = FetchType.EAGER)
    private List<GarageEmployee> employees = new LinkedList<>();

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

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

//    public List<GarageEmployee> getEmployees() {
//        return employees;
//    }
//
//    public void setEmployees(List<GarageEmployee> employees) {
//        this.employees = employees;
//    }
}
