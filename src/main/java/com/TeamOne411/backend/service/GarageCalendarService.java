package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import com.TeamOne411.backend.entity.schedule.TimeSlot;
import com.TeamOne411.backend.repository.GarageCalendarRepository;
import com.TeamOne411.backend.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

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

    public void saveGarageCalendar(GarageCalendar garageCalendar){
        garageCalendarRepository.save(garageCalendar);
    }

    public void saveTimeSlot(TimeSlot timeSlot){
        timeSlotRepository.save(timeSlot);
    }

    public void deleteGarageCalendar(GarageCalendar garageCalendar){
        garageCalendarRepository.delete(garageCalendar);
    }

    public GarageCalendar findByGarage(Garage garage){
        return garageCalendarRepository.findByGarage(garage);
    }

}
