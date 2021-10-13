package com.ownwear.app.repository;

import com.ownwear.app.entity.Comment;
import com.ownwear.app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(long commentid);

//    List<Comment> findAllByPost(Long postid);

    List<Comment> findAllByPost(Post post);
    Long countAllByPostPostid(long postId);
}
