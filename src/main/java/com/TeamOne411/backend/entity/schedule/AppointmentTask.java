package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;

/**
 This entity defines the service that is to be performed (or was performed) for a particular appointment.
 Allows the Garage employee to specify price and/or duration if different from the standard offered service
 */
@Entity
public class AppointmentTask extends AbstractEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "offered_service_id")
    private OfferedService offeredService;

    @NotNull
    private Duration duration;

    @NotNull
    private BigDecimal price;

    private String appointmentComments;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public OfferedService getOfferedService() {
        return offeredService;
    }

    public void setOfferedService(OfferedService offeredService) {
        this.offeredService = offeredService;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAppointmentComments() {
        return appointmentComments;
    }

    public void setAppointmentComments(String appointmentComments) {
        this.appointmentComments = appointmentComments;
    }
}