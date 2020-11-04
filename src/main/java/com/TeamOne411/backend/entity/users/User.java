package com.TeamOne411.backend.entity.users;

import com.TeamOne411.backend.entity.AbstractEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="discriminator")
public class User extends AbstractEntity {
    @NotNull
    @NotEmpty
    @Length(min = 3, max = 15, message = "Username must be between 3 and 15 characters.")
    private String username = "";

    @NotNull
    @NotEmpty(message = "Password can't be empty.")
    @Length(min = 8, max = 150, message = "Password must be at least 8 characters.")
    private String password = "";

    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @NotNull
    @NotEmpty(message = "First name can't be empty.")
    private String firstName = "";

    @NotNull
    @NotEmpty(message = "Last name can't be empty.")
    private String lastName = "";

    @NotNull
    @NotEmpty(message = "Email address can't be empty.")
    @Email(message = "Email address must be valid format.")
    private String email = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
