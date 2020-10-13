package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.users.Privilege;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
    Privilege findByName(String name);
}
