package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.entity.schedule.AppointmentTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentTaskRepository extends JpaRepository<AppointmentTask, Long> {
    List<AppointmentTask> findAllByAppointment(Appointment appointment);
}
