package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.users.GarageEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarageEmployeeRepository extends JpaRepository<GarageEmployee, Long> {
}
