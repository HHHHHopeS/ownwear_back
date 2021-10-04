package com.ownwear.app.repository;

import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.LikePost;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;


public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

    ArrayList<PostHashTag> findByPost(Post post);

    @Query("select p.hashtag from PostHashTag p where p.post = ?1")
    List<HashTag> findHashTagsByPost(Post post);
}
