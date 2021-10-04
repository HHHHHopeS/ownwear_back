package com.ownwear.app.repository;

import com.ownwear.app.model.LikePost;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface LikePostRepository extends JpaRepository<LikePost, Long> {

        int countByPost(Post post);
}
