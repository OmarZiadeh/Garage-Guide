package com.TeamOne411.backend.entity.servicecatalog;

import com.TeamOne411.backend.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Duration;

@Entity
public class OfferedService extends AbstractEntity {
    @NotNull
    @Size(min = 2, message = "Service name must have at least two characters")
    private String serviceName = "";
    private String serviceDescription = "";

    @Min(0)
    private BigDecimal price = BigDecimal.ZERO;

//  @Min validator doesn't exist for type Duration, we can protect against negatives in service layer
//    @Min(0)
    private Duration duration = Duration.ZERO;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "service_category_id")
    private ServiceCategory serviceCategory;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /*
    TODO: Add set and get functions to the calling function which creates the Offered Service.
     */
    public String getServiceDescription(){ return serviceDescription; }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    /*
    TODO: Add set and get functions to calling function which creates the Offered Service.
     */
    public void setDuration(Duration duration) { this.duration = duration; }

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