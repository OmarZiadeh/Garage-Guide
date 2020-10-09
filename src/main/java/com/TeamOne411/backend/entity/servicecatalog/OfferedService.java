package com.TeamOne411.backend.entity.servicecatalog;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OfferedService implements Serializable {

    @NotNull
    private int id = -1;
    @NotNull
    @Size(min = 2, message = "Service name must have at least two characters")
    private String serviceName = "";
    @Min(0)
    private BigDecimal price = BigDecimal.ZERO;
    private Set<ServiceCategory> serviceCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<ServiceCategory> getServiceCategory() {
        return serviceCategory;
    }

    public void setCategory(Set<ServiceCategory> serviceCategory) {
        this.serviceCategory = serviceCategory;
    }
    
    public boolean isNewService() {
        return getId() == -1;
    }

    /*
     * Vaadin DataProviders rely on properly implemented equals and hashcode
     * methods.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || id == -1) {
            return false;
        }
        if (obj instanceof OfferedService) {
            return id == ((OfferedService) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (id == -1) {
            return super.hashCode();
        }

        return Objects.hash(id);
    }
}