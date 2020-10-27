package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
}