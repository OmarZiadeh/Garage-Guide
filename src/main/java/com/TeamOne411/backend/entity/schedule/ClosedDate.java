package com.TeamOne411.backend.entity.schedule;

import com.TeamOne411.backend.entity.AbstractEntity;
import com.TeamOne411.backend.entity.Garage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 This entity defines a date that the Garage is closed outside of their weekly days closed - such as Holidays.
 */
@Entity
public class ClosedDate extends AbstractEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @NotNull
    @Column(name = "not_open_date", columnDefinition = "DATE")
    private LocalDate notOpenDate;

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public LocalDate getNotOpenDate() {
        return notOpenDate;
    }

    public void setNotOpenDate(LocalDate dateClosed) {
        this.notOpenDate = dateClosed;
    }
}
