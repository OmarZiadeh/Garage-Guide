package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.entity.schedule.AppointmentTask;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.repository.AppointmentRepository;
import com.TeamOne411.backend.repository.AppointmentTaskRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    /**
     * Finds upcoming appointments for a garage
     * @param garage the garage to search by
     * @return List of appointments
     */
    public List<Appointment> findAllUpcomingAppointmentsByGarage(Garage garage) {
        return appointmentRepository.findAllByGarageAndAppointmentDateGreaterThanOrderByAppointmentDateAscAppointmentTimeAsc(garage, LocalDate.now());
    }

    /**
     * Finds today's appointments for a garage
     * @param garage the garage to search by
     * @return List of appointments
     */
    public List<Appointment> findAllAppointmentsTodayByGarage(Garage garage) {
        return appointmentRepository.findAllByGarageAndAppointmentDateEqualsOrderByAppointmentTime(garage, LocalDate.now());
    }

    /**
     * Finds today's appointments for a car owner
     * @return List of appointments
     */
    public List<Appointment> findAllAppointmentsForToday(CarOwner carOwner){
        return appointmentRepository.findAllByAppointmentDateEqualsAndVehicle_CarOwnerOrderByAppointmentTime(LocalDate.now(), carOwner);
    }


    /**
     * Finds all upcoming appointments for a car owner
     * @return List of appointments
     */
    public List<Appointment> findAllUpcomingAppointments(CarOwner carOwner){
        return appointmentRepository.findAllByAppointmentDateGreaterThanAndVehicle_CarOwnerOrderByAppointmentDateAscAppointmentTimeAsc(LocalDate.now(),carOwner);
    }

    /**
     * Finds all past appointments
     * @return List of appointments
     */
    public List<Appointment> findAllPastAppointments(){
        return appointmentRepository.findAllByAppointmentDateLessThanOrderByAppointmentDate(LocalDate.now());
    }

    public void saveAppointmentTask(AppointmentTask appointmentTask) {
        appointmentTaskRepository.save(appointmentTask);
    }

    public void deleteAppointmentTask(AppointmentTask appointmentTask){
        appointmentTaskRepository.delete(appointmentTask);
    }

    /**
     * Deletes all appointment tasks for a given appointment
     * @param appointment the appointment to search by
     */
    public void deleteAppointmentTasks(Appointment appointment) {
        List<AppointmentTask> appointmentTasks = findAllAppointmentTasksByAppointment(appointment);
        for(AppointmentTask appointmentTask : appointmentTasks){
            appointmentTaskRepository.delete(appointmentTask);
        }
    }

    /**
     * Finds all appointment tasks for a given appointment
     * @param appointment the appointment to search by
     * @return the list of appointment tasks
     */
    public List<AppointmentTask> findAllAppointmentTasksByAppointment(Appointment appointment) {
        return (appointmentTaskRepository.findAllByAppointment(appointment));
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