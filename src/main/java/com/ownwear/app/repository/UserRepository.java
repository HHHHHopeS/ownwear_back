package com.ownwear.app.repository;

import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(long user_id);


    Boolean existsByEmail(String email);

    List<User> findAllBySex(boolean sex);
}
