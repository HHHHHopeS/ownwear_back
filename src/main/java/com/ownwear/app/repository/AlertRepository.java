package com.ownwear.app.repository;

import com.ownwear.app.model.Alert;
import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("select a from Alert a where a.user = ?1 and a.ischecked = false")
    List<Alert> findFalseByUser(User user);
}
