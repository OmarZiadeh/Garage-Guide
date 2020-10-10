package com.TeamOne411.backend.entity.users;

import com.TeamOne411.backend.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
public class Privilege extends AbstractEntity {
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege() {
        super();
    }

    public Privilege(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}