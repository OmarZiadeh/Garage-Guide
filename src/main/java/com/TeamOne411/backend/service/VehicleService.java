package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.Vehicle;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import com.TeamOne411.backend.repository.CarOwnerRepository;
import com.TeamOne411.backend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VehicleService {
    private static final Logger LOGGER = Logger.getLogger(VehicleService.class.getName());
    private VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> findByCarOwner(CarOwner carOwner) {
        return vehicleRepository.findByCarOwner(carOwner);
    }

    public long count() {
        return vehicleRepository.count();
    }

    public void delete(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

    public void save(Vehicle vehicle) {
        if (vehicle == null) {
            LOGGER.log(Level.SEVERE,
                    "Vehicle is null. Are you sure you have connected your form to the application?");
            return;
        }
        vehicleRepository.save(vehicle);
    }

    public void registerNewVehicle(Vehicle vehicle) {
    }

    public void updateVehicle(Vehicle vehicle) {
    }
}
