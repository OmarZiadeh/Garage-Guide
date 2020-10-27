package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferedServiceRepository extends JpaRepository<OfferedService, Long> {
}