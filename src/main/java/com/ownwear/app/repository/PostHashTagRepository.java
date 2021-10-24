package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexHashTag;
import com.ownwear.app.dto.IIndexPost;
import com.ownwear.app.entity.HashTag;
import com.ownwear.app.entity.Post;
import com.ownwear.app.entity.PostHashTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;


public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

    ArrayList<PostHashTag> findByPost(Post post);

    @Query("select p.hashtag from PostHashTag p where p.post = ?1")
    List<HashTag> findHashTagsByPost(Post post);

    void deleteAllByPost(Post post);

    @Query(value = "SELECT Count(p.hashtagid) as count,h.hashtagid, h.hashtagname  " +
            "FROM Post_Hash_Tag p " +
            "NATURAL JOIN HASH_TAG h " +
            "GROUP BY p.hashtagid " +
            "HAVING Count(p.hashtagid) >= 1 " +
            "ORDER BY Count(p.hashtagid) desc LIMIT 9;",nativeQuery = true)
    List<IIndexHashTag> findTop9ByCountByHashtagInterface();

    String result = "SELECT p.postid as postid, p.imgdata as imgdata, p.user as user " +
            "from Post p " +
            "where p.postid IN " +
            "(SELECT ph.post " +
            "from PostHashTag ph join HashTag h on ph.hashtag.hashtagid = h.hashtagid " +
            "where h.hashtagname = ?1)";
    @Query(result)
    Page<IIndexPost> findByCountByHashtag(String tagname, Pageable pageable);// 태그 리스트

    @Query(value = "SELECT Count(p.hashtagid) as count,h.hashtagid, h.hashtagname  " +
            "FROM Post_Hash_Tag p " +
            "NATURAL JOIN HASH_TAG h " +
            "GROUP BY p.hashtagid " +
            "HAVING Count(p.hashtagid) >= 1 " +
            "ORDER BY Count(p.hashtagid) desc LIMIT 9;",nativeQuery = true)
    List<PostHashTag> findTop6PostByHashTag();
}
