package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.entity.users.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByAppointmentDateGreaterThanAndVehicle_CarOwnerOrderByAppointmentDateAscAppointmentTimeAsc(LocalDate localDate, CarOwner carOwner);
    List<Appointment> findAllByAppointmentDateEqualsAndVehicle_CarOwnerOrderByAppointmentTime(LocalDate localDate, CarOwner carOwner);
    List<Appointment> findAllByAppointmentDateLessThanOrderByAppointmentDate(LocalDate localDate);
    List<Appointment> findAllByGarageAndAppointmentDateGreaterThanOrderByAppointmentDateAscAppointmentTimeAsc(Garage garage, LocalDate localDate);
    List<Appointment> findAllByGarageAndAppointmentDateEqualsOrderByAppointmentTime(Garage garage, LocalDate localDate);
}