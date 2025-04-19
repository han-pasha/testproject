package com.problem.test.mapper;

import com.problem.test.dto.ProductDTO;
import com.problem.test.model.Product;

/**
 * Usually this class would be simple if we are using mapstruct.
 * But for simplistic reason, we are going with traditional approach.
 */
public abstract class ProductMapper {
  public static ProductDTO toDto(Product incoming) {
   return ProductDTO.builder()
              .id(incoming.getId())
              .name(incoming.getProductName())
              .price(incoming.getProductPrice())
              .build();
  }

  public static Product toProduct(ProductDTO incoming) {
    return Product.builder()
               .id(incoming.getId())
               .productName(incoming.getName())
               .productPrice(incoming.getPrice())
               .build();
  }
}
