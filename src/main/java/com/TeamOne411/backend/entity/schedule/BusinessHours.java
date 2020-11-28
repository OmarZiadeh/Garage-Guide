package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalTime;

/**
This entity defines the opening and closing times for the business for a particular day of the week
 */
@Entity
public class BusinessHours extends AbstractEntity {
    @NotNull
    private String dayOfTheWeek;

    @NotNull
    private int dayNumber;

    @NotNull
    private Boolean isOpen = Boolean.FALSE;

    @Column(name = "open_time", columnDefinition = "TIME")
    private LocalTime openTime;

    @Column(name = "close_time", columnDefinition = "TIME")
    private LocalTime closeTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
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

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public long subtractTimes(){
        return Duration.between(openTime, closeTime).toMinutes();
    }
}
