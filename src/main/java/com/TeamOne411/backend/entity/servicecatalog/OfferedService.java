package com.TeamOne411.backend.entity.servicecatalog;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;

import java.math.BigDecimal;
import java.time.Duration;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class OfferedService extends AbstractEntity {

    @NotNull
    @Size(min = 2, message = "Service name must have at least two characters")
    private String serviceName = "";
    @NotNull
    @ManyToOne
    @JoinColumn(name = "service_category_id")
    private ServiceCategory serviceCategory;
    @NotNull
    private Duration duration;
    @Min(0)
    @NotNull
    private BigDecimal price;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ServiceCategory getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

}