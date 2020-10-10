package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
    public User findByUsername(@Param("username") String username);

    public User findByEmail(@Param("email") String email);
}