package com.ownwear.app.repository;

import com.ownwear.app.model.CurrentUsers;
import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrentUsersRepository extends JpaRepository<CurrentUsers, Long> {

    Optional<CurrentUsers> findById(long id);


}