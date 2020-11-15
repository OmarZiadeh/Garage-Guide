package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {
    public User findByPhoneNumber(String phone);
}
