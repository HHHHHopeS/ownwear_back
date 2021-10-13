package com.ownwear.app.repository;

import com.ownwear.app.entity.Follow;
import com.ownwear.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    @Query("SELECT f FROM Follow f where f.fromuser = :from and f.touser = :to")
    Optional<Follow> findByUsers(User from, User to);

    long countByTouser(User user);

    List<Follow> findAllByTouser(User user);
}
