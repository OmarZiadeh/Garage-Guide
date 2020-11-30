package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.entity.schedule.ClosedDate;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import com.TeamOne411.backend.entity.schedule.TimeSlot;
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

    public GarageCalendar findCalendarByGarage(Garage garage) {
        return garageCalendarRepository.findCalendarByGarage(garage);
    }

    public List<TimeSlot> findTimeSlotsByGarage(Garage garage){
        return timeSlotRepository.findTimeSlotsByGarage(garage);
    }

    public List<ClosedDate> findClosedDatesByGarage(Garage garage){
        return closedDateRepository.findClosedDatesByGarageOrderByNotOpenDate(garage);
    }

    public ClosedDate findClosedDateByGarage(Garage garage, LocalDate localDate){
        return closedDateRepository.findClosedDateByGarageAndNotOpenDateEquals(garage, localDate);
    }

    /**
     * This generates the available appointment time slots for a garage
     */
    @Async("threadPoolTaskExecutor")
    public void generateTimeSlots(GarageCalendar garageCalendar, BusinessHoursService businessHoursService) {
        System.out.println("TimeSlot creation thread started");
        for (LocalDate date = garageCalendar.getCalendarStartDate();
             date.isBefore(garageCalendar.getCalendarEndDate().plusDays(1));
             date = date.plusDays(1)) {

            ClosedDate closedDate = findClosedDateByGarage(garageCalendar.getGarage(), date);
            if(closedDate == null){
                BusinessHours businessHours = businessHoursService.findByDayNumberAndGarage(date.getDayOfWeek().getValue(),
                        garageCalendar.getGarage());
                if(businessHours.getOpen()) {
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
}
