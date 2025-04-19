package com.problem.test.rest;

import com.problem.test.dto.ProductDTO;
import com.problem.test.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductResourceTest {

  private ProductService productService;
  private ProductResource productResource;

  @BeforeEach
  void setUp() throws Exception {
    productService = mock(ProductService.class);
    productResource = new ProductResource();

    Field serviceField = ProductResource.class.getDeclaredField("productService");
    serviceField.setAccessible(true);
    serviceField.set(productResource, productService);
  }

  @Test
  void testGetAllProducts() {
    ProductDTO product = new ProductDTO(1L, "Test Product", "123");
    when(productService.findPaginated(0, 10)).thenReturn(Collections.singletonList(product));

    Response response = productResource.getAllProducts(0, 10);

    assertEquals(200, response.getStatus());
    assertTrue(response.getEntity() instanceof List<?>);
    assertEquals(1, ((List<?>) response.getEntity()).size());
  }

  @Test
  void testGetProductById_found() {
    ProductDTO product = new ProductDTO(1L, "Product A", "200");
    when(productService.getProductById(1L)).thenReturn(product);

    Response response = productResource.getProductById(1L);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals(product, response.getEntity());
  }

  @Test
  void testGetProductById_notFound() {
    when(productService.getProductById(2L)).thenReturn(null);

    Response response = productResource.getProductById(2L);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void testCreateProduct() throws Exception {
    ProductDTO input = new ProductDTO(null, "New Product", "500");
    ProductDTO saved = new ProductDTO(10L, "New Product", "500");

    when(productService.saveProduct(input)).thenReturn(saved);

    // Mock UriInfo and inject it
    UriInfo uriInfo = mock(UriInfo.class);
    UriBuilder uriBuilder = mock(UriBuilder.class);
    when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
    when(uriBuilder.path("10")).thenReturn(uriBuilder);
    when(uriBuilder.build()).thenReturn(URI.create("http://localhost/products/10"));

    // Inject UriInfo manually (assuming you use a setter method or public field)
    productResource.setUriInfo(uriInfo);

    Response response = productResource.createProduct(input);

    assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    assertEquals(saved, response.getEntity());
    assertEquals("http://localhost/products/10", response.getLocation().toString());
  }

  @Test
  void testUpdateProduct_found() {
    ProductDTO input = new ProductDTO(null, "Updated", "321");
    ProductDTO updated = new ProductDTO(1L, "Updated", "321");

    when(productService.updateProduct(any(ProductDTO.class))).thenReturn(updated);

    Response response = productResource.updateProduct(1L, input);

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals(updated, response.getEntity());
  }

  @Test
  void testUpdateProduct_notFound() {
    ProductDTO input = new ProductDTO(null, "Updated", "321");

    when(productService.updateProduct(any(ProductDTO.class))).thenReturn(null);

    Response response = productResource.updateProduct(1L, input);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
  }

  @Test
  void testDeleteProduct() {
    Response response = productResource.deleteProduct(1L);
    assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
  }
}
