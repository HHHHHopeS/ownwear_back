package com.ownwear.app.repository;

import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username); // UserDetail 페이지

    Optional<User> findById(long user_id);

    Boolean existsByEmail(String email);

    List<User> findAllBySex(boolean sex); // 성별로 유저 search

}
