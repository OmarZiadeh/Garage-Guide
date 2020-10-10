package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.users.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    public UserAccount findByUserName(@Param("userName") String userName);

    public UserAccount findByEmailAddress(@Param("emailAddress") String emailAddress);
}