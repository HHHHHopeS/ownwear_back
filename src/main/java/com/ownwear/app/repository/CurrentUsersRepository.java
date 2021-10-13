package com.ownwear.app.repository;

import com.ownwear.app.entity.CurrentUsers;
import com.ownwear.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrentUsersRepository extends JpaRepository<CurrentUsers, Long> {

    Optional<CurrentUsers> findByUser(User user);

    void deleteByUser(User user);
}
