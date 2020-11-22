package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Back-end service interface for appointment CRUD operations
 */
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void saveAppointment(Appointment appointment){
        appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Appointment appointment){
        appointmentRepository.delete(appointment);
    }

    public List<Appointment> findAll() {
        return (appointmentRepository.findAll());
    }
}