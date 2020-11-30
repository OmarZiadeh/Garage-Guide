package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.entity.schedule.AppointmentTask;
import com.TeamOne411.backend.repository.AppointmentRepository;
import com.TeamOne411.backend.repository.AppointmentTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Back-end service interface for appointment CRUD operations
 */
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    public final AppointmentTaskRepository appointmentTaskRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentTaskRepository appointmentTaskRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentTaskRepository = appointmentTaskRepository;
    }

    public void saveAppointment(Appointment appointment){
        appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Appointment appointment){
        appointmentRepository.delete(appointment);
    }

    public List<Appointment> findAllAppointments() {
        return (appointmentRepository.findAll());
    }

    public void saveAppointmentTask(AppointmentTask appointmentTask){
        appointmentTaskRepository.save(appointmentTask);
    }

    public void deleteAppointmentTask(AppointmentTask appointmentTask){
        appointmentTaskRepository.delete(appointmentTask);
    }

    public List<AppointmentTask> findAllAppointmentTasks() {
        return (appointmentTaskRepository.findAll());
    }
}