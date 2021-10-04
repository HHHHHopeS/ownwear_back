package com.ownwear.app.repository;

import com.ownwear.app.form.PostForm;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import org.springframework.data.domain.Page;
<<<<<<< Updated upstream
import org.springframework.data.domain.PageRequest;
=======
>>>>>>> Stashed changes
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< Updated upstream
import java.util.ArrayList;
=======
>>>>>>> Stashed changes
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    ArrayList<Post> findByUser(User user);

<<<<<<< Updated upstream
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByUserIn(List<User> user, Pageable pageable);

    Page<Post> findAllByUser(User users, Pageable pageable);

    void deleteById(long id);

=======
    //    Optional<Post> findByPost_id(Long post_id);
//    Page<Post> findById(long post_id, Pageable pageable);
>>>>>>> Stashed changes
}
