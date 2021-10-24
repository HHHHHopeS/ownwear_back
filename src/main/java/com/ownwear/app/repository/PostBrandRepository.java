package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexBrand;
import com.ownwear.app.dto.IIndexPost;
import com.ownwear.app.dto.IndexBrand;
import com.ownwear.app.entity.Post;
import com.ownwear.app.entity.PostBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostBrandRepository extends JpaRepository<PostBrand, Long> {

    @Query(value = "SELECT Count(p.brandid) as count, p.brandid , b.brandname " +
            "FROM Post_brand p " +
            "NATURAL JOIN Brand b " +
            "GROUP BY p.brandid " +
            "HAVING Count(p.brandid) >= 1 " +
            "ORDER BY Count(p.brandid) desc LIMIT 9;",nativeQuery = true)
    List<IIndexBrand> findTop9ByCountByBrand();

    String findbyCount_ = "SELECT post.postid as postid,post.imgdata as imgdata, post.user as user " +
            "from Post post where post.postid IN " +
            "(SELECT p.post " +
            "from PostBrand p join Brand b on p.brand.brandid = b.brandid " +
            "where b.brandname = ?1)";
    @Query(findbyCount_)
    Page<IIndexPost> findByCountByBrand(String a , Pageable pageable); //브랜드 리스트

    @Query(value = "SELECT p.postid " +
            "FROM Post_brand p " +
            "NATURAL JOIN Brand b " +
            "GROUP BY p.brandid " +
            "HAVING Count(p.brandid) >= 1 " +
            "ORDER BY Count(p.brandid) desc  LIMIT 6;",nativeQuery = true)
    List<Long> findTop6PostidByBrand();
    //, po.imgData as imgData
}