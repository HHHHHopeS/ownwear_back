package com.ownwear.app.repository;

import com.ownwear.app.form.IIndexUser;
import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username); // UserDetail 페이지

    Optional<User> findById(Long id); //유저 My page

    Boolean existsByEmail(String email);

    List<User> findAllBySex(boolean sex); // 성별로 유저 search


    @Query(value = "SELECT Count(f.to_user) as count, u.user_id , u.username, u.userimg " +
            "FROM follow f " +
            "Join user u ON u.user_id = f.to_user " +
            "GROUP BY f.to_user " +
            "HAVING Count(f.to_user) >= 1 " +
            "ORDER BY Count(f.to_user) desc LIMIT 7;",nativeQuery = true)
    List<IIndexUser> findTop7ByFollow();

    List<User> findByUsernameStartsWith(String username);
}
