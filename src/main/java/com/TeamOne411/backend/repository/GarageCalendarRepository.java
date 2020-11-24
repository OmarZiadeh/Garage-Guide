package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarageCalendarRepository extends JpaRepository<GarageCalendar, Long> {
    GarageCalendar findByGarage(Garage garage);
}