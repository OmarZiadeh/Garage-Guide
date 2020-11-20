package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
public class GarageSchedule extends AbstractEntity {
    @NotNull
    @OneToOne
    @JoinColumn(name = "garage_id", referencedColumnName = "id")
    private Garage garage;

    @NotNull
    @Column(name = "calendar_start_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime calendarStartDate;

    @NotNull
    @Column(name = "calendar_end_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime calendarEndDate;

    @OneToMany(mappedBy = "garageSchedule")
    private List<TimeSlot> timeSlots = new LinkedList<>();

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public LocalDateTime getCalendarStartDate() {
        return calendarStartDate;
    }

    public void setCalendarStartDate(LocalDateTime calendarStartDate) {
        this.calendarStartDate = calendarStartDate;
    }

    public LocalDateTime getCalendarEndDate() {
        return calendarEndDate;
    }

    public void setCalendarEndDate(LocalDateTime calendarEndDate) {
        this.calendarEndDate = calendarEndDate;
    }
/*
    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

 */
}
