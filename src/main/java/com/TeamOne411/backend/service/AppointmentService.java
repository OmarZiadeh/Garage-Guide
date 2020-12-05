package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.entity.schedule.AppointmentTask;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.repository.AppointmentRepository;
import com.TeamOne411.backend.repository.AppointmentTaskRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Back-end service interface for appointment CRUD operations
 */
@Service
public class AppointmentService {

    public final AppointmentTaskRepository appointmentTaskRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentTaskRepository appointmentTaskRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentTaskRepository = appointmentTaskRepository;
    }

    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Appointment appointment) {
        appointmentRepository.delete(appointment);
    }

    public List<Appointment> findAllAppointments() {
        return (appointmentRepository.findAll());
    }

    public void saveAppointmentTask(AppointmentTask appointmentTask) {
        appointmentTaskRepository.save(appointmentTask);
    }

    public void deleteAppointmentTask(AppointmentTask appointmentTask) {
        appointmentTaskRepository.delete(appointmentTask);
    }

    public List<AppointmentTask> findAllAppointmentTasks() {
        return (appointmentTaskRepository.findAll());
    }

    /**
     * Creates the appointment Tasks for an appointment
     *
     * @param appointment     the appointment that the tasks are associated with
     * @param offeredServices the offered services the tasks should be created based on
     */
    @Async("threadPoolTaskExecutor")
    public void createAppointmentTasks(Appointment appointment, Set<OfferedService> offeredServices) {
        System.out.println("Starting Appointment Task Creation");
        for (OfferedService os : offeredServices) {
            AppointmentTask appointmentTask = new AppointmentTask();
            appointmentTask.setPrice(os.getPrice());
            appointmentTask.setDuration(os.getDuration());
            appointmentTask.setAppointment(appointment);
            appointmentTask.setOfferedService(os);
            saveAppointmentTask(appointmentTask);
        }
        System.out.println("Appointment Task Creation Completed");
    }
}