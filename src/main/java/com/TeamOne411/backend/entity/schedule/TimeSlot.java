package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class TimeSlot extends AbstractEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "garage_schedule_id")
    private GarageSchedule garageSchedule;

    @NotNull
    @Column(name = "start_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime startDateTime;

    @NotNull
    private Boolean isFilled = Boolean.FALSE;

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

    public Boolean getFilled() {
        return isFilled;
    }

    public void setFilled(Boolean filled) {
        isFilled = filled;
    }
}
