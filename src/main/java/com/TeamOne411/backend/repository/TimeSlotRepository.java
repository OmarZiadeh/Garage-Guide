package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    @Query("SELECT startTime FROM TimeSlot where garage = :garage and startDate = :start_date")
    List<LocalTime> findStartTimeByGarageAndStartDateEqualsAndIsFilledIsFalse(@Param("garage") Garage garage,
                                                                              @Param("start_date") LocalDate localDate);
}
