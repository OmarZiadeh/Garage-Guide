package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.users.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
