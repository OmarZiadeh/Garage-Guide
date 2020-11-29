package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 This entity defines the calendar for the Garage - the start and end dates for when the Garage will accept appointments
 */
@Entity
public class GarageCalendar extends AbstractEntity {
    @NotNull
    @OneToOne
    @JoinColumn(name = "garage_id", referencedColumnName = "id")
    private Garage garage;

    @NotNull
    @Column(name = "calendar_start_date", columnDefinition = "DATE")
    private LocalDate calendarStartDate;

    @NotNull
    @Column(name = "calendar_end_date", columnDefinition = "DATE")
    private LocalDate calendarEndDate;

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public LocalDate getCalendarStartDate() {
        return calendarStartDate;
    }

    public void setCalendarStartDate(LocalDate calendarStartDate) {
        this.calendarStartDate = calendarStartDate;
    }

    public LocalDate getCalendarEndDate() {
        return calendarEndDate;
    }

    public void setCalendarEndDate(LocalDate calendarEndDate) {
        this.calendarEndDate = calendarEndDate;
    }

}