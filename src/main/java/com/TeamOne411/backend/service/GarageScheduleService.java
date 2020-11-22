package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.GarageSchedule;
import com.TeamOne411.backend.repository.GarageScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Back-end service interface for managing the Garage Schedule
 */
@Service
public class GarageScheduleService {

    private final GarageScheduleRepository garageScheduleRepository;

    public GarageScheduleService(GarageScheduleRepository garageScheduleRepository) {
        this.garageScheduleRepository = garageScheduleRepository;
    }

    public void saveGarageSchedule(GarageSchedule garageSchedule){
        garageScheduleRepository.save(garageSchedule);
    }

    public GarageSchedule findByGarage(Garage garage){
        return garageScheduleRepository.findByGarage(garage);
    }

    /**
     * This method creates the initial schedule for the garage
     */
    public void initializeGarageSchedule(Garage garage){
        GarageSchedule garageSchedule = new GarageSchedule();
        garageSchedule.setGarage(garage);
        garageSchedule.setCalendarStartDate(LocalDateTime.now());
        garageSchedule.setCalendarEndDate(LocalDateTime.now().plusDays(2));
        garageScheduleRepository.save(garageSchedule);
    }
}
