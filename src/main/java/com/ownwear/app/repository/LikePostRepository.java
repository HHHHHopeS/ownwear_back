package com.ownwear.app.repository;

import com.ownwear.app.model.LikePost;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long>{

    @Query(value = "SELECT * FROM Like_Post l  GROUP BY l.postid HAVING Count(l.postid) >= 1 ORDER BY Count(l.postid) desc LIMIT 6;" , nativeQuery = true)
    List<LikePost> findTop6ByPost();

    ArrayList<LikePost> findByPost(Post post);

    Optional<LikePost> findByUserAndPost(User user, Post post);

    List<LikePost> findByUser(User user);

    long countByPost(Post post); // 유저 좋아요 갯수 조회
}
