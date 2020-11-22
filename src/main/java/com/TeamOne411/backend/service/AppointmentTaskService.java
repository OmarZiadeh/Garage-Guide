package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.schedule.AppointmentTask;
import com.TeamOne411.backend.repository.AppointmentTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Back-end service interface for Appointment Task (service to be performed / completed) CRUD operations
 */
@Service
public class AppointmentTaskService {

    public final AppointmentTaskRepository appointmentTaskRepository;

    public AppointmentTaskService(AppointmentTaskRepository appointmentTaskRepository) {
        this.appointmentTaskRepository = appointmentTaskRepository;
    }

    public void saveAppointmentTask(AppointmentTask appointmentTask){
        appointmentTaskRepository.save(appointmentTask);
    }

    public void deleteAppointmentTask(AppointmentTask appointmentTask){
        appointmentTaskRepository.delete(appointmentTask);
    }

    public List<AppointmentTask> findAll() {
        return (appointmentTaskRepository.findAll());
    }
}