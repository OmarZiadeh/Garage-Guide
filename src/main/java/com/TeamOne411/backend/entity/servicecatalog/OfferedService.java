package com.TeamOne411.backend.entity.servicecatalog;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;

import java.math.BigDecimal;
import java.time.Duration;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class OfferedService extends AbstractEntity {

    @NotNull
    @Size(min = 2, message = "Service name must have at least two characters")
    private String serviceName = "";
    private String serviceDescription = "";
    @NotNull
    private ServiceCategory serviceCategory;
    //TODO Need to determine the appropriate default for duration
    private Duration duration = Duration.ZERO;
    @Min(0)
    private BigDecimal price = BigDecimal.ZERO;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
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

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }
}