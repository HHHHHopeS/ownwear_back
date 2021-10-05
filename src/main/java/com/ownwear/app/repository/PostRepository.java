package com.ownwear.app.repository;

import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    ArrayList<Post> findByUser(User user);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByUserIn(List<User> user, Pageable pageable);

    Page<Post> findAllByUser(User users, Pageable pageable);

    void deleteById(long id);

    @Query("select p from Post p where p.user = ?1")
    List<Post> findAllByUser(User user); //유저 디테일 페이지
}
