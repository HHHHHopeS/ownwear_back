package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexBrand;
import com.ownwear.app.dto.RankingBrand_IF;
import com.ownwear.app.entity.Brand;
import com.ownwear.app.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT b.brandid as brandid,b.brandname as brandName,count(p.brand.brandid) as counts, p.post as post from Brand b JOIN PostBrand p on p.brand.brandid = b.brandid  group by b.brandid order by counts desc")
    Page<IIndexBrand> findRankingData(boolean sex, Pageable pageRequest);

    List<Brand> findByBrandnameStartsWith(String brandname);

    Optional<Brand> findByBrandname(String brandString);

    @Query("SELECT p FROM Post p  join PostBrand pb on pb.post.postid = p.postid where pb.brand.brandid = :brandid")
    List<Post> findAllByBrandid(long brandid);
}
