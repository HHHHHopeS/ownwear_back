package com.ownwear.app.repository;

import com.ownwear.app.form.IIndexHashTag;
import com.ownwear.app.model.HashTag;
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

    void deleteAllByPost(Post post);

    @Query(value = "SELECT Count(p.hashtag_id) as count,h.hashtag_id, h.hashtagname  " +
            "FROM Post_Hash_Tag p " +
            "NATURAL JOIN HASH_TAG h " +
            "GROUP BY p.hashtag_id " +
            "HAVING Count(p.hashtag_id) >= 1 " +
            "ORDER BY Count(p.hashtag_id) desc LIMIT 9;",nativeQuery = true)
    List<IIndexHashTag> findTop9ByCountByHashtagInterface();
}
