package com.problem.test.model;

import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This is the entity class for Product.
 * </P> The reason we are not using BigDecimal as the data type is encryption.
 */
@Entity
@Builder
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String productName;
  private String productPrice;

  public Product() {
  }

  public Product(String productName, String productPrice) {
    this.productName = productName;
    this.productPrice = productPrice;
  }

  public Product(Long id, String productName, String productPrice) {
    this.id = id;
    this.productName = productName;
    this.productPrice = productPrice;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(String productPrice) {
    this.productPrice = productPrice;
  }
}
