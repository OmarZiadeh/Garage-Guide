package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.schedule.AppointmentTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentTaskRepository extends JpaRepository<AppointmentTask, Long> {
}
