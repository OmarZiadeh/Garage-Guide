package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findTimeSlotsByGarage(Garage garage);
}
