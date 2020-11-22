package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.entity.schedule.GarageSchedule;
import com.TeamOne411.backend.repository.BusinessHoursRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Back-end service interface for setting and retrieving business hours
 */
@Service
public class BusinessHoursService {
    private final BusinessHoursRepository businessHoursRepository;

    public BusinessHoursService(BusinessHoursRepository businessHoursRepository) {
        this.businessHoursRepository = businessHoursRepository;
    }

    public void saveBusinessHours(BusinessHours businessHours){
        businessHoursRepository.save(businessHours);
    }

    public List<BusinessHours> findAll() {
        return (businessHoursRepository.findAll());
    }

    /**
     * This method assigns a business hours placeholder for each day of the week
     *
     * @param garageSchedule The garageSchedule the business hours should be associated with
     */
    public void initializeBusinessHours(GarageSchedule garageSchedule){
        createBusinessDay(garageSchedule, "MONDAY");
        createBusinessDay(garageSchedule, "TUESDAY");
        createBusinessDay(garageSchedule, "WEDNESDAY");
        createBusinessDay(garageSchedule, "THURSDAY");
        createBusinessDay(garageSchedule, "FRIDAY");
        createBusinessDay(garageSchedule, "SATURDAY");
        createBusinessDay(garageSchedule, "SUNDAY");
    }

    /**
     * This method creates the business hours placeholder
     *
     * @param garageSchedule The garageSchedule the business hours should be associated with
     * @param name The name for the day of the week
     */
    public void createBusinessDay(GarageSchedule garageSchedule, String name){
        BusinessHours businessHours = new BusinessHours();
        businessHours.setGarageSchedule(garageSchedule);
        businessHours.setDayOfWeek(name);
        businessHoursRepository.save(businessHours);
    }
}