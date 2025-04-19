package com.problem.test.mapper;
import com.problem.test.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductDTOValidationTest {
  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void whenProductNameIsNull_thenValidationFails() {
    ProductDTO dto = new ProductDTO();
    dto.setPrice("100");

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
  }

  @Test
  void whenProductPriceIsNull_thenValidationFails() {
    ProductDTO dto = new ProductDTO();
    dto.setName("Item");

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price")));
  }

  @Test
  void whenAllFieldsValid_thenValidationPasses() {
    ProductDTO dto = new ProductDTO();
    dto.setName("Item");
    dto.setPrice("100");

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
    assertTrue(violations.isEmpty());
  }

  @Test
  public void whenProductDTOIsValid_thenNoConstraintViolations() {
    ProductDTO dto = new ProductDTO();
    dto.setId(1L);
    dto.setName("Valid Name");
    dto.setPrice("12345");

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
    assertTrue(violations.isEmpty());
  }

  @Test
  public void whenFieldsAreBlank_thenConstraintViolations() {
    ProductDTO dto = new ProductDTO();
    dto.setName(""); // should trigger @NotBlank
    dto.setPrice(" "); // should trigger @NotBlank

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
    assertEquals(3, violations.size());
  }
}
