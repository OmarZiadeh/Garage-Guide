package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.repository.GarageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GarageService {
    private static final Logger LOGGER = Logger.getLogger(GarageService.class.getName());
    private GarageRepository garageRepository;

    public GarageService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    public List<Garage> findAll() {
        return garageRepository.findAll();
    }

    public long count() {
        return garageRepository.count();
    }

    public void delete(Garage garage) {
        garageRepository.delete(garage);
    }

    public void save(Garage garage) {
        if (garage == null) {
            LOGGER.log(Level.SEVERE,
                    "Garage is null. Are you sure you have connected your form to the application?");
            return;
        }
        garageRepository.save(garage);
    }
}
