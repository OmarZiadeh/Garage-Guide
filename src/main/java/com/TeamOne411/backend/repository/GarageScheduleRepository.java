package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.schedule.GarageSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarageScheduleRepository extends JpaRepository<GarageSchedule, Long> {
}
