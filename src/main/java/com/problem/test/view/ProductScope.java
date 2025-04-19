package com.problem.test.view;

import com.problem.test.dto.ProductDTO;
import com.problem.test.model.Product;
import com.problem.test.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class ProductScope implements Serializable {

  private List<ProductDTO> products;
  private ProductDTO selectedProduct;
  private boolean formVisible;

  private int page = 1;
  private final int pageSize = 10;

  private static final Logger log = LoggerFactory.getLogger(ProductScope.class);

  @Inject
  private ProductService productService;

  @PostConstruct
  public void init() {
    products = productService.findPaginated(page, pageSize);
    selectedProduct = new ProductDTO();
    formVisible = false;
  }

  public List<ProductDTO> getPaginatedProducts() {
    if (products == null || products.isEmpty()) {
      return Collections.emptyList();
    }
    return products;
  }

  public void nextPage() {
    if(page < getTotalPages()){
      page++;
    }
  }

  public void previousPage() {
    if(page > 1){
      page--;
    }
  }

  // Returns total pages for pagination controls
  public int getTotalPages() {
    if (products == null || products.isEmpty()){
      return 1;
    }
    return (int) Math.ceil((double) products.size() / pageSize);
  }

  // Getters and setters for page and others
  public int getPage() {
    return page;
  }
  public void setPage(int page) {
    this.page = page;
  }

  public List<ProductDTO> getProducts() {
    return products;
  }
  public void setProducts(List<ProductDTO> products) {
    this.products = products;
  }
  public ProductDTO getSelectedProduct() {
    return selectedProduct;
  }
  public void setSelectedProduct(ProductDTO selectedProduct) {
    this.selectedProduct = selectedProduct;
  }
  public boolean isFormVisible() {
    return formVisible;
  }
  public void setFormVisible(boolean formVisible) {
    this.formVisible = formVisible;
  }

  // Adds a new product.
  public void addProduct() {
    try {
      productService.saveProduct(selectedProduct);
      products = productService.findPaginated(page, pageSize);
      resetState();
    } catch (Exception e) {
      // Keep form open and show error messages
      formVisible = true;
      log.error("Error adding product", e);
    }
  }

  public void updateProduct() {
    try {
      productService.updateProduct(selectedProduct);
      // Only hide form if successful
      page = 0;
      this.setProducts(productService.findPaginated(page, pageSize));
      selectedProduct = new ProductDTO();
      formVisible = false;
    } catch (Exception e) {
      // Keep form open on error
      formVisible = true;
      log.error("Error updating product", e);
    }
  }

  public void editProduct(ProductDTO product) {
    selectedProduct = product;
    formVisible = true;
  }

  public void deleteProduct(Long id) {
    productService.deleteProduct(id);
    products = productService.findPaginated(page, pageSize);
    // Ensure current page is still valid after deletion.
    if (page > getTotalPages()){
      page = getTotalPages();
    }
  }

  public void resetState() {
    this.selectedProduct = new ProductDTO();
  }
}
