package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.*;
import com.TeamOne411.backend.repository.ClosedDateRepository;
import com.TeamOne411.backend.repository.GarageCalendarRepository;
import com.TeamOne411.backend.repository.TimeSlotRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Back-end service interface for managing the Garage Calendar and appointment Time Slots
 */
@Service
public class GarageCalendarService {

    private final GarageCalendarRepository garageCalendarRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ClosedDateRepository closedDateRepository;

    public GarageCalendarService(GarageCalendarRepository garageScheduleRepository,
                                 TimeSlotRepository timeSlotRepository,
                                 ClosedDateRepository closedDateRepository) {
        this.garageCalendarRepository = garageScheduleRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.closedDateRepository = closedDateRepository;
    }

    public void saveClosedDate(ClosedDate closedDate) {
        closedDateRepository.save(closedDate);
    }

    public void deleteClosedDate(ClosedDate closedDate) {
        closedDateRepository.delete(closedDate);
    }

    public void saveGarageCalendar(GarageCalendar garageCalendar) {
        garageCalendarRepository.save(garageCalendar);
    }

    public void saveTimeSlot(TimeSlot timeSlot) {
        timeSlotRepository.save(timeSlot);
    }

    /**
     * Finds the calendar for a garage
     *
     * @param garage the garage to search by
     * @return The garage calendar found in the repository
     */
    public GarageCalendar findCalendarByGarage(Garage garage) {
        return garageCalendarRepository.findCalendarByGarage(garage);
    }

    /**
     * Finds a time slot for a specified garage on a specified date at a specified time and is not already filled
     *
     * @param garage    the garage object to search by
     * @param localDate the date to search by
     * @param localTime the time to search by
     * @return the time slot found
     */
    public TimeSlot findTimeSlotByGarageAndStartDateAndStartTime(Garage garage, LocalDate localDate, LocalTime localTime) {
        return timeSlotRepository.findTimeSlotByGarageAndStartDateAndStartTimeAndIsFilledIsFalse(garage, localDate, localTime);
    }

    /**
     * Returns a list of available appointment times for a garage on a certain date (time slots are not filled)
     *
     * @param garage    the garage object to search by
     * @param localDate the date to search by
     * @return The list of available appointment times
     */
    public List<LocalTime> findStartTimesByGarageAndDate(Garage garage, LocalDate localDate) {
        return timeSlotRepository.findStartTimesByGarageAndStartDateEqualsAndIsFilledIsFalse(garage, localDate);
    }

    /**
     * Returns a list of closed dates for a garage
     *
     * @param garage the garage object to search by
     * @return List of closed dates for the garage
     */
    public List<ClosedDate> findClosedDatesByGarage(Garage garage) {
        return closedDateRepository.findClosedDatesByGarageOrderByNotOpenDate(garage);
    }

    /**
     * Finds a ClosedDate object for a garage on a certain date
     *
     * @param garage    the garage object to search by
     * @param localDate the date to search by
     * @return the ClosedDate object found in the repository
     */
    public ClosedDate findClosedDateByGarage(Garage garage, LocalDate localDate) {
        return closedDateRepository.findClosedDateByGarageAndNotOpenDateEquals(garage, localDate);
    }

    /**
     * Finds all garages if a garage calendar exists
     */
    public List<Garage> findAllByGarageExists(){
        return garageCalendarRepository.findAllByGarageExists();
    }


    /**
     * This generates the available appointment time slots for a garage
     *
     * @param businessHoursService the business hours service for managing the business hours
     * @param garageCalendar       the garage calendar the time slots will be associated with
     */
    @Async("threadPoolTaskExecutor")
    public void generateTimeSlots(GarageCalendar garageCalendar, BusinessHoursService businessHoursService) {
        System.out.println("TimeSlot creation thread started");
        for (LocalDate date = garageCalendar.getCalendarStartDate();
             date.isBefore(garageCalendar.getCalendarEndDate().plusDays(1));
             date = date.plusDays(1)) {

            ClosedDate closedDate = findClosedDateByGarage(garageCalendar.getGarage(), date);
            if (closedDate == null) {
                BusinessHours businessHours = businessHoursService.findByDayNumberAndGarage(date.getDayOfWeek().getValue(),
                        garageCalendar.getGarage());
                if (businessHours.getOpen()) {
                    long minutes = businessHours.subtractTimes();
                    for (long i = 0; i < minutes; i += 30) {
                        Duration d = Duration.ZERO.plusMinutes(i);
                        LocalTime time = businessHours.getOpenTime().plus(d);
                        TimeSlot timeSlot = new TimeSlot();
                        timeSlot.setGarage(garageCalendar.getGarage());
                        timeSlot.setStartDate(date);
                        timeSlot.setStartTime(time);
                        saveTimeSlot(timeSlot);
                    }
                }
            }
        }
        System.out.println("TimeSlot creation complete");
    }

    /**
     * This fills the garage time slots to prevent double booking
     *
     * @param appointment the appointment that is filling the time slots
     */
    @Async("threadPoolTaskExecutor")
    public void fillTimeSlots(Appointment appointment) {
        System.out.println("Starting To Fill Time Slots");
        LocalDate date = appointment.getAppointmentDate();
        long minutes = appointment.getEstimatedDuration().toMinutes();
        for (long i = 0; i < minutes; i += 30) {
            Duration d = Duration.ZERO.plusMinutes(i);
            LocalTime time = appointment.getAppointmentTime().plus(d);
            TimeSlot timeSlot = findTimeSlotByGarageAndStartDateAndStartTime(appointment.getGarage(), date, time);
            if (timeSlot != null) {
                timeSlot.setFilled(true);
                saveTimeSlot(timeSlot);
            } else {
                break;
            }
        }
        System.out.println("Finished Filling Time Slots");
    }
}
