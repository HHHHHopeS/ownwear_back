package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexBrand;
import com.ownwear.app.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findByBrandnameStartsWith(String brandname);

    Optional<Brand> findByBrandname(String brandString);
}
