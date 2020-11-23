package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 This entity defines an appointment that has been scheduled with the garage
 */
@Entity
public class Appointment extends AbstractEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @NotNull
    @Column(name = "start_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime startDateTime;

    @NotNull
    private Duration estimatedDuration;

    @NotNull
    private String status = new String("Not Started");

    @Column(name = "estimated_completion", columnDefinition = "TIMESTAMP")
    private LocalDateTime estimatedCompletion;

    private String statusComments;

    //TODO Add attribute & relationship to Vehicle

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
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

    public LocalDateTime getEstimatedCompletion() {
        return estimatedCompletion;
    }

    public void setEstimatedCompletion(LocalDateTime estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    public String getStatusComments() {
        return statusComments;
    }

    public void setStatusComments(String statusComments) {
        this.statusComments = statusComments;
    }
}
