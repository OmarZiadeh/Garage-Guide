package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.Vehicle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This entity defines an appointment that has been scheduled with a garage
 */
@Entity
public class Appointment extends AbstractEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @NotNull
    @Column(name = "appointment_date", columnDefinition = "DATE")
    private LocalDate appointmentDate;

    @NotNull
    @Column(name = "appointment_time", columnDefinition = "TIME")
    private LocalTime appointmentTime;

    @NotNull
    private Duration estimatedDuration;

    @NotNull
    private BigDecimal estimatedTotalPrice;

    @NotNull
    private String status = ("Not Started");

    @Column(name = "estimated_completion_time", columnDefinition = "TIME")
    private LocalTime estimatedCompletionTime;

    private String statusComments;

    private String carOwnerComments;

    public String getCarOwnerComments() {
        return carOwnerComments;
    }

    public void setCarOwnerComments(String carOwnerComments) {
        this.carOwnerComments = carOwnerComments;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Duration duration) {
        this.estimatedDuration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusComments() {
        return statusComments;
    }

    public void setStatusComments(String statusComments) {
        this.statusComments = statusComments;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public LocalTime getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(LocalTime estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    public BigDecimal getEstimatedTotalPrice() {
        return estimatedTotalPrice;
    }

    public void setEstimatedTotalPrice(BigDecimal estimatedTotalPrice) {
        this.estimatedTotalPrice = estimatedTotalPrice;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
