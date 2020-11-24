package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
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

    public void deleteBusinessHours(List<BusinessHours> businessHours){
        businessHoursRepository.deleteAll(businessHours);
    }

    public List<BusinessHours> findByGarage(Garage garage) {
        return (businessHoursRepository.findByGarage(garage));
    }

    /**
     * This method assigns a business hours placeholder for each day of the week
     *
     * @param garage The garageSchedule the business hours should be associated with
     */
    public void initializeBusinessHours(Garage garage){
        createBusinessDay(garage, "MONDAY");
        createBusinessDay(garage, "TUESDAY");
        createBusinessDay(garage, "WEDNESDAY");
        createBusinessDay(garage, "THURSDAY");
        createBusinessDay(garage, "FRIDAY");
        createBusinessDay(garage, "SATURDAY");
        createBusinessDay(garage, "SUNDAY");
    }

    /**
     * This method creates the business hours placeholder
     *
     * @param garage The garage the business hours should be associated with
     * @param name The name for the day of the week
     */
    public void createBusinessDay(Garage garage, String name){
        BusinessHours businessHours = new BusinessHours();
        businessHours.setGarage(garage);
        businessHours.setDayOfTheWeek(name);
        businessHoursRepository.save(businessHours);
    }
}