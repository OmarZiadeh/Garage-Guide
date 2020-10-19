package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.repository.GarageEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GarageEmployeeService {
    private static final Logger LOGGER = Logger.getLogger(GarageEmployeeService.class.getName());
    private GarageEmployeeRepository garageEmployeeRepository;

    public GarageEmployeeService(GarageEmployeeRepository garageEmployeeRepository) {
        this.garageEmployeeRepository = garageEmployeeRepository;
    }

    public List<GarageEmployee> findAll() {
        return garageEmployeeRepository.findAll();
    }

    public List<GarageEmployee> findByGarage(Garage employer) {
        return garageEmployeeRepository.findByGarage(employer);
    }

    public long count() {
        return garageEmployeeRepository.count();
    }

    public void delete(GarageEmployee garageEmployee) {
        garageEmployeeRepository.delete(garageEmployee);
    }

    public void save(GarageEmployee garageEmployee) {
        if (garageEmployee == null) {
            LOGGER.log(Level.SEVERE,
                    "GarageEmployee is null. Are you sure you have connected your form to the application?");
            return;
        }
        garageEmployeeRepository.save(garageEmployee);
    }
}
