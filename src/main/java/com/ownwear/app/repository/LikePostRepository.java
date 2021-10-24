package com.ownwear.app.repository;

import com.ownwear.app.entity.LikePost;
import com.ownwear.app.entity.Post;
import com.ownwear.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long>{

    @Query(value = "SELECT l FROM LikePost l  GROUP BY l.post.postid , l.likepostid HAVING Count(l.post.postid) >= 1 ORDER BY Count(l.post.postid)")
    List<LikePost> findTop6ByPost();

    @Query(value = "SELECT l FROM LikePost l JOIN Post p ON l.post.postid = p.postid and l.post.user.sex = ?1 GROUP BY l.post.postid, l.likepostid HAVING Count(l.post.postid) >= 1  ORDER BY Count(l.post.postid) desc")
    List<LikePost> findTop6ByPost(Boolean sex);

    ArrayList<LikePost> findByPost(Post post);

    Optional<LikePost> findByUserAndPost(User user, Post post);

    List<LikePost> findByUser(User user);

    long countByPost(Post post); // 유저 좋아요 갯수 조회

    Long countByPostPostid(Long postid);

    List<LikePost> findAllByPost(Post targetPost);
}
