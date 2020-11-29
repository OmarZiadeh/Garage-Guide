package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.ClosedDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClosedDateRepository extends JpaRepository<ClosedDate, Long> {
    List<ClosedDate> findClosedDatesByGarageOrderByNotOpenDate(Garage garage);
    ClosedDate findClosedDateByGarageAndNotOpenDateEquals(Garage garage, LocalDate localDate);
}
