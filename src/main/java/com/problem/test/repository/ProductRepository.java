package com.problem.test.repository;

import com.problem.test.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByProductName(String productName);
  Page<Product> findAllByOrderByIdDesc(Pageable pageable);
}
