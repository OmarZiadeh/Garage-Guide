package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.repository.CarOwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CarOwnerService {
    private static final Logger LOGGER = Logger.getLogger(CarOwnerService.class.getName());
    private CarOwnerRepository carOwnerRepository;

    public CarOwnerService(CarOwnerRepository carOwnerRepository) {
        this.carOwnerRepository = carOwnerRepository;
    }

    public List<CarOwner> findAll() {
        return carOwnerRepository.findAll();
    }

    public long count() {
        return carOwnerRepository.count();
    }

    public void delete(CarOwner carOwner) {
        carOwnerRepository.delete(carOwner);
    }

    public void save(CarOwner carOwner) {
        if (carOwner == null) {
            LOGGER.log(Level.SEVERE,
                    "CarOwner is null. Are you sure you have connected your form to the application?");
            return;
        }
        carOwnerRepository.save(carOwner);
    }
}
