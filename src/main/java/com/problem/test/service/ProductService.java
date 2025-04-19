package com.problem.test.service;

import com.problem.test.dto.ProductDTO;
import com.problem.test.model.Product;

import java.util.List;

public interface ProductService {
  ProductDTO getProductById(Long id);
  List<ProductDTO> getProductsByName(String name);
  List<ProductDTO> findPaginated(int page, int size);
  ProductDTO saveProduct(ProductDTO product);
  ProductDTO updateProduct(ProductDTO product);
  void deleteProduct(Long id);
}
