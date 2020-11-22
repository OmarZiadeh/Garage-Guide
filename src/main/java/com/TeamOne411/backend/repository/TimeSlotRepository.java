package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.schedule.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}
