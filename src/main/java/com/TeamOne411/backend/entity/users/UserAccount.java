package com.TeamOne411.backend.entity.users;

import com.TeamOne411.backend.entity.AbstractEntity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public abstract class UserAccount extends AbstractEntity {
    @NotNull
    @NotEmpty
    private String userName = "";

    @NotNull
    @NotEmpty
    private String firstName = "";

    @NotNull
    @NotEmpty
    private String lastName = "";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
