package com.example.eshop.repository;

import com.example.eshop.entity.CategoryEntity;
import com.example.eshop.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<ProductEntity,Long> {

    List<ProductEntity> findAllByNameContaining(String name);

    List<ProductEntity> findAllByCategory(CategoryEntity category);

    Page<ProductEntity> findAllByNameContaining(String name, Pageable pageable);

    Page<ProductEntity> findAllByCategory(CategoryEntity category, Pageable pageable);
}
