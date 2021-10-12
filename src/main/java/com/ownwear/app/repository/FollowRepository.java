package com.ownwear.app.repository;

import com.ownwear.app.model.Follow;
import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    @Query("SELECT f FROM Follow f where f.from = :from and f.to = :to")
    Optional<Follow> findByUsers(User from, User to);
}
