package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.GarageCalendar;
import com.TeamOne411.backend.repository.GarageScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Back-end service interface for managing the Garage Schedule
 */
@Service
public class GarageCalendarService {

    private final GarageScheduleRepository garageScheduleRepository;

    public GarageCalendarService(GarageScheduleRepository garageScheduleRepository) {
        this.garageScheduleRepository = garageScheduleRepository;
    }

    public void saveGarageSchedule(GarageCalendar garageCalendar){
        garageScheduleRepository.save(garageCalendar);
    }

    public void deleteGarageSchedule(GarageCalendar garageCalendar){
        garageScheduleRepository.delete(garageCalendar);
    }

    public GarageCalendar findByGarage(Garage garage){
        return garageScheduleRepository.findByGarage(garage);
    }

    /**
     * This method creates the initial schedule for the garage
     */
    public void initializeGarageSchedule(Garage garage){
        GarageCalendar garageCalendar = new GarageCalendar();
        garageCalendar.setGarage(garage);
        garageCalendar.setCalendarStartDate(LocalDateTime.now());
        garageCalendar.setCalendarEndDate(LocalDateTime.now().plusDays(2));
        garageScheduleRepository.save(garageCalendar);
    }
}
