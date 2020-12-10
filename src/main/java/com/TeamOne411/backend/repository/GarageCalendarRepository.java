package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GarageCalendarRepository extends JpaRepository<GarageCalendar, Long> {
    GarageCalendar findCalendarByGarage(Garage garage);

    @Query("SELECT garage FROM GarageCalendar")
    List<Garage> findAllByGarageExists();
}