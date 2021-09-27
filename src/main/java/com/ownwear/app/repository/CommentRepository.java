package com.ownwear.app.repository;

import com.ownwear.app.model.Comment;
import com.ownwear.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByCommentno(long commentno);

    ArrayList<Comment> findByPostno(long Postno);



}
