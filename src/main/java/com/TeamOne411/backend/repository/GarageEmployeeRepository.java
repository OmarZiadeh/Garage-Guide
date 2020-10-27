package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.users.GarageEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GarageEmployeeRepository extends JpaRepository<GarageEmployee, Long> {
    List<GarageEmployee> findByGarage(Garage garage);
}
