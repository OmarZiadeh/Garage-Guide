package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.users.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {
}
