package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.Vehicle;
import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    public List<Vehicle> findByCarOwner(CarOwner carOwner);
}
