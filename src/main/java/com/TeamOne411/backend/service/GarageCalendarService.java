package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import com.TeamOne411.backend.entity.schedule.TimeSlot;
import com.TeamOne411.backend.repository.GarageCalendarRepository;
import com.TeamOne411.backend.repository.TimeSlotRepository;
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

    public GarageCalendarService(GarageCalendarRepository garageScheduleRepository,
                                 TimeSlotRepository timeSlotRepository) {
        this.garageCalendarRepository = garageScheduleRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public void saveGarageCalendar(GarageCalendar garageCalendar) {
        garageCalendarRepository.save(garageCalendar);
    }

    public void saveTimeSlot(TimeSlot timeSlot) {
        timeSlotRepository.save(timeSlot);
    }


    public void deleteTimeSlots(List<TimeSlot> timeSlots){
        timeSlotRepository.deleteAll(timeSlots);
    }

    public List<TimeSlot> findTimeSlotsByGarage(Garage garage){
        return timeSlotRepository.findTimeSlotsByGarage(garage);
    }

    public void deleteGarageCalendar(GarageCalendar garageCalendar) {
        deleteTimeSlots(findTimeSlotsByGarage(garageCalendar.getGarage()));
        garageCalendarRepository.delete(garageCalendar);
    }

    public GarageCalendar findByGarage(Garage garage) {
        return garageCalendarRepository.findByGarage(garage);
    }

    /**
     * This generates the available appointment time slots for a garage
     */
    public void generateTimeSlots(GarageCalendar garageCalendar, BusinessHoursService businessHoursService) {
        for (LocalDate date = garageCalendar.getCalendarStartDate();
             date.isBefore(garageCalendar.getCalendarEndDate().plusDays(1));
             date = date.plusDays(1)) {

            BusinessHours businessHours = businessHoursService.findByDayNumberAndGarage(date.getDayOfWeek().getValue(),
                    garageCalendar.getGarage());
            if(businessHours.getOpen()) {
                long minutes = businessHours.subtractTimes();
                for(long i = 0; i < minutes; i += 30){
                    Duration d = Duration.ZERO.plusMinutes(i);
                    LocalTime time = businessHours.getOpenTime().plus(d);
                    TimeSlot timeSlot = new TimeSlot();
                    timeSlot.setGarage(garageCalendar.getGarage());
                    timeSlot.setStartDate(date);
                    timeSlot.setStartTime(time);
                    timeSlotRepository.save(timeSlot);
                }
            }
        }
    }
}
