package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.repository.BusinessHoursRepository;
import org.springframework.scheduling.annotation.Async;
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

    public void saveBusinessHours(BusinessHours businessHours) {
        businessHoursRepository.save(businessHours);
    }

    public void deleteBusinessHours(List<BusinessHours> businessHours) {
        businessHoursRepository.deleteAll(businessHours);
    }

    public List<BusinessHours> findByGarage(Garage garage) {
        return (businessHoursRepository.findByGarage(garage));
    }

    public BusinessHours findByDayNumberAndGarage(int dayNumber, Garage garage) {
        return businessHoursRepository.findByDayNumberAndGarage(dayNumber, garage);
    }

    /**
     * This method assigns a business hours placeholder for each day of the week
     *
     * @param garage The garage the business hours should be associated with
     */
    @Async("threadPoolTaskExecutor")
    public void initializeBusinessHours(Garage garage) {
        createBusinessDay(garage, "Monday", 1);
        createBusinessDay(garage, "Tuesday", 2);
        createBusinessDay(garage, "Wednesday", 3);
        createBusinessDay(garage, "Thursday", 4);
        createBusinessDay(garage, "Friday", 5);
        createBusinessDay(garage, "Saturday", 6);
        createBusinessDay(garage, "Sunday", 7);
    }

    /**
     * This method creates the business hours placeholder
     *
     * @param garage The garage the business hours should be associated with
     * @param name   The name for the day of the week
     */
    public void createBusinessDay(Garage garage, String name, int number) {
        BusinessHours businessHours = new BusinessHours();
        businessHours.setGarage(garage);
        businessHours.setDayOfTheWeek(name);
        businessHours.setDayNumber(number);
        businessHoursRepository.save(businessHours);
    }
}