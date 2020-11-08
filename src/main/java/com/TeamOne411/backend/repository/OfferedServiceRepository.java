package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferedServiceRepository extends JpaRepository<OfferedService, Long> {
    List<OfferedService> findServicesByServiceCategory(ServiceCategory serviceCategory);
}