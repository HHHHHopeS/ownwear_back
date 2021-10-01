package com.ownwear.app.repository;

import com.ownwear.app.form.PostForm;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    ArrayList<Post> findByUser(User user);

//   Page<Post> findByUser(List<User> user, Pageable pageable);
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByUserIn(List<User> user, Pageable pageable);

    Page<Post> findAllByUser(User users, Pageable pageable);

//    Page<Post> findAll(User user, Pageable pageable);

}
