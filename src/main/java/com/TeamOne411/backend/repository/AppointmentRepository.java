package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.schedule.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository  extends JpaRepository<Appointment, Long> {
}
