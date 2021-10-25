package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexBrand;
import com.ownwear.app.dto.IIndexPost;
import com.ownwear.app.entity.PostBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostBrandRepository extends JpaRepository<PostBrand, Long> {

    @Query(value = "SELECT Count(p.brandid) as counts, p.brandid , b.brandname " +
            "FROM post_brand p " +
            "NATURAL JOIN brand b " +
            "GROUP BY p.brandid " +
            "HAVING Count(p.brandid) >= 1 " +
            "ORDER BY Count(p.brandid) desc LIMIT 9;",nativeQuery = true)
    List<IIndexBrand> findTop9ByCountByBrand();

    @Query(value = "SELECT p.postid " +
            "FROM post_brand p " +
            "NATURAL JOIN brand b " +
            "GROUP BY p.brandid, p.post_brandid " +
            "HAVING Count(p.brandid) >= 1 " +
            "ORDER BY Count(p.brandid) desc  LIMIT 6;",nativeQuery = true)
    List<Long> findTop6PostidByBrand();

    @Query(value = "SELECT p.post.postid " +
            "FROM PostBrand p " +
            "JOIN Brand b " +
            "ON p.brand.brandid = b.brandid and p.post.user.sex = ?1 " +
            "GROUP BY p.brand.brandid, p.post_brandid " +
            "HAVING Count(p.brand.brandid) >= 1 " +
            "ORDER BY Count(p.brand.brandid) desc")
    List<Long> findTop6PostidByBrand(Boolean sex);

    String findbyCount_ = "SELECT post.postid as postid,post.imgdata as imgdata, post.user as user " +
            "from Post post where post.postid IN " +
            "(SELECT p.post " +
            "from PostBrand p join Brand b on p.brand.brandid = b.brandid " +
            "where b.brandname = ?1)";
    @Query(findbyCount_)
    Page<IIndexPost> findByCountByBrand(String a , Pageable pageable);



    //, po.imgData as imgData
}
