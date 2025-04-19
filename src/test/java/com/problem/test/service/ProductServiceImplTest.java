package com.problem.test.service;

import com.problem.test.dto.ProductDTO;
import com.problem.test.mapper.ProductMapper;
import com.problem.test.model.Product;
import com.problem.test.repository.ProductRepository;
import com.problem.test.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static com.problem.test.util.AesEncryptionUtil.decrypt;
import static com.problem.test.util.AesEncryptionUtil.encrypt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductServiceImpl productService;

  private Product sampleProduct;
  private ProductDTO sampleProductDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    sampleProduct = new Product();
    sampleProduct.setId(1L);
    sampleProduct.setProductName("Sample");
    sampleProduct.setProductPrice(encrypt("1000"));

    sampleProductDTO = new ProductDTO();
    sampleProductDTO.setId(1L);
    sampleProductDTO.setName("Sample");
    sampleProductDTO.setPrice("1000");
  }

  @Test
  void testGetProductById_found() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
    ProductDTO result = productService.getProductById(1L);
    assertEquals("Sample", result.getName());
  }

  @Test
  void testGetProductById_notFound() {
    when(productRepository.findById(2L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> productService.getProductById(2L));
  }

  @Test
  void testGetProductsByName_validName() {
    when(productRepository.findByProductName("Sample")).thenReturn(Collections.singletonList(sampleProduct));
    List<ProductDTO> result = productService.getProductsByName("Sample");
    assertFalse(result.isEmpty());
  }

  @Test
  void testGetProductsByName_blankName() {
    List<ProductDTO> result = productService.getProductsByName(" ");
    assertTrue(result.isEmpty());
  }

  @Test
  void testFindPaginated() {
    Page<Product> page = new PageImpl<>(Arrays.asList(sampleProduct));
    when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
    List<ProductDTO> result = productService.findPaginated(1, 10);
    assertEquals(1, result.size());
  }

  @Test
  void testSaveProduct_valid() {
    Product mappedProduct = ProductMapper.toProduct(sampleProductDTO);
    when(productRepository.save(any())).thenReturn(mappedProduct);
    ProductDTO result = productService.saveProduct(sampleProductDTO);
    assertEquals("Sample", result.getName());
  }

  @Test
  void testSaveProduct_nullInput() {
    assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(null));
  }

  @Test
  void testUpdateProduct_valid() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
    when(productRepository.save(any())).thenReturn(ProductMapper.toProduct(sampleProductDTO));
    ProductDTO result = productService.updateProduct(sampleProductDTO);
    assertEquals("Sample", result.getName());
  }

  @Test
  void testUpdateProduct_missingId() {
    ProductDTO invalidDto = new ProductDTO();
    assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(invalidDto));
  }

  @Test
  void testUpdateProduct_notFound() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(sampleProductDTO));
  }

  @Test
  void testDeleteProduct_validId() {
    doNothing().when(productRepository).deleteById(1L);
    assertDoesNotThrow(() -> productService.deleteProduct(1L));
  }

  @Test
  void testDeleteProduct_notFound() {
    doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(2L);
    assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(2L));
  }
}
