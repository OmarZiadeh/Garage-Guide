package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.schedule.TimeSlot;
import com.TeamOne411.backend.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

/**
 * Back-end service interface for appointment time slots
 */
@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public void saveTimeSlot(TimeSlot timeSlot){
        timeSlotRepository.save(timeSlot);
    }
}
