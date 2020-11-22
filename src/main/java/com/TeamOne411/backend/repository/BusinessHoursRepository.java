package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.schedule.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {
}
