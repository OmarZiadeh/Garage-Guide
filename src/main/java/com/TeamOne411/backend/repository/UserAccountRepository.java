package com.TeamOne411.backend.repository;

import com.TeamOne411.backend.entity.users.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    @Query(value = "SELECT u.user_name FROM user_account u WHERE u.user_name = :userName", nativeQuery = true)
    public UserAccount getUserByUsername(@Param("userName") String userName);
}