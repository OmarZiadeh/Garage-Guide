package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
This entity defines the opening and closing times for the business for a particular day of the week
 */
@Entity
public class BusinessHours extends AbstractEntity {
    @NotNull
    private String dayOfWeek;

    @NotNull
    private Boolean isOpen = Boolean.FALSE;

    @Column(name = "open_time", columnDefinition = "TIME")
    private LocalTime openTime;

    @Column(name = "close_time", columnDefinition = "TIME")
    private LocalTime closeTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "garage_schedule_id")
    private GarageSchedule garageSchedule;

    public GarageSchedule getGarageSchedule() {
        return garageSchedule;
    }

    public void setGarageSchedule(GarageSchedule garageSchedule) {
        this.garageSchedule = garageSchedule;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }
}
