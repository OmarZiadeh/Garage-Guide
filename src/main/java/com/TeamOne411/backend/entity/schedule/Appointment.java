package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class Appointment extends AbstractEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "garage_schedule_id")
    private GarageSchedule garageSchedule;

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

    public GarageSchedule getGarageSchedule() {
        return garageSchedule;
    }

    public void setGarageSchedule(GarageSchedule garageSchedule) {
        this.garageSchedule = garageSchedule;
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
