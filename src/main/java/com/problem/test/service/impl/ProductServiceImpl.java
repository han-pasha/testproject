package com.problem.test.service.impl;

import com.problem.test.dto.ProductDTO;
import com.problem.test.mapper.ProductMapper;
import com.problem.test.model.Product;
import com.problem.test.repository.ProductRepository;
import com.problem.test.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.problem.test.util.AesEncryptionUtil.decrypt;
import static com.problem.test.util.AesEncryptionUtil.encrypt;

@Service
public class ProductServiceImpl implements ProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public ProductDTO getProductById(Long id) {
    log.info("Fetching product with ID: {}", id);
    return productRepository.findById(id)
               .map(product -> {
                 log.debug("Decrypting product price for ID: {}", product.getId());
                 product.setProductPrice(decrypt(product.getProductPrice()));
                 return product;
               }).map(ProductMapper::toDto)
               .orElseThrow(() -> {
                 log.warn("Product not found with ID: {}", id);
                 return new EntityNotFoundException("Product not found with ID: " + id);
               });
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProductDTO> getProductsByName(String name) {
    log.info("Searching products by name: '{}'", name);
    if (name == null || name.trim().isEmpty()) {
      log.debug("Empty product name provided. Returning empty list.");
      return Collections.emptyList();
    }

    List<ProductDTO> products = productRepository.findByProductName(name).stream()
                                    .peek(p -> {
                                      log.debug("Decrypting price for product ID: {}", p.getId());
                                      p.setProductPrice(decrypt(p.getProductPrice()));
                                    })
                                    .map(ProductMapper::toDto)
                                    .collect(Collectors.toList());

    log.info("Found {} products with name: '{}'", products.size(), name);
    return products;
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProductDTO> findPaginated(int page, int size) {
    log.info("Fetching paginated products. Page: {}, Size: {}", page, size);
    Pageable pageable = PageRequest.of(page - 1, size);
    Page<Product> paginatedProducts = productRepository.findAllByOrderByIdDesc(pageable);

    List<ProductDTO> result = paginatedProducts.getContent().stream()
                                  .peek(product -> {
                                    log.debug("Decrypting price for product ID: {}", product.getId());
                                    product.setProductPrice(decrypt(product.getProductPrice()));
                                  })
                                  .map(ProductMapper::toDto)
                                  .collect(Collectors.toList());

    log.info("Returning {} products for page {}", result.size(), page);
    return result;
  }

  @Transactional
  @Override
  public ProductDTO saveProduct(ProductDTO productDto) {
    log.info("Saving new product: {}", productDto);
    if (productDto == null) {
      log.error("Attempted to save null product");
      throw new IllegalArgumentException("Product cannot be null");
    }

    Product product = ProductMapper.toProduct(productDto);
    product.setProductPrice(encrypt(product.getProductPrice()));
    Product saved = productRepository.save(product);
    log.info("Product saved with ID: {}", saved.getId());
    return ProductMapper.toDto(saved);
  }

  @Transactional
  @Override
  public ProductDTO updateProduct(ProductDTO productDto) {
    log.info("Updating product: {}", productDto);
    if (productDto == null || productDto.getId() == null) {
      log.error("Product or Product ID is null");
      throw new IllegalArgumentException("Product or Product ID must not be null");
    }

    Optional<Product> productWithId = productRepository.findById(productDto.getId());
    if (!productWithId.isPresent()) {
      log.warn("Attempted to update non-existing product with ID: {}", productDto.getId());
      throw new EntityNotFoundException("Cannot update — product not found with ID: " + productDto.getId());
    }

    Product product = ProductMapper.toProduct(productDto);
    product.setProductPrice(encrypt(product.getProductPrice()));
    Product updated = productRepository.save(product);
    log.info("Product updated with ID: {}", updated.getId());
    return ProductMapper.toDto(updated);
  }

  @Transactional
  @Override
  public void deleteProduct(Long id) {
    log.info("Attempting to delete product with ID: {}", id);
    try {
      productRepository.deleteById(id);
      log.info("Successfully deleted product with ID: {}", id);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Deletion failed. No product found with ID: {}", id);
      throw new EntityNotFoundException("Product not found for deletion with ID: " + id);
    }
  }
}

