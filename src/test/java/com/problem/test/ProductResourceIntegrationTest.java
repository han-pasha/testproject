package com.problem.test;

import com.problem.test.dto.ProductDTO;
import com.problem.test.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductResourceIntegrationTest {

  private int port = 9000;

  private String baseUrl = "http://localhost:9000/api/mobile/products";

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll(); // clear database before each test
  }

  @Test
  void testCreateProduct() {
    RestTemplate restTemplate = new RestTemplate();
    ProductDTO dto = new ProductDTO(null, "Test Product", "123");

    ResponseEntity<ProductDTO> response = restTemplate.postForEntity(
        baseUrl + "/save", dto, ProductDTO.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertNotNull(response.getBody().getId());
  }

  @Test
  void testGetAllProductsPagination() {
    ProductDTO p1 = new ProductDTO(null, "Phone", "500");
    ProductDTO p2 = new ProductDTO(null, "Tablet", "700");

    String createBaseUrl = baseUrl + "/save";

    restTemplate.postForEntity(createBaseUrl, p1, ProductDTO.class);
    restTemplate.postForEntity(createBaseUrl, p2, ProductDTO.class);

    ResponseEntity<ProductDTO[]> response = restTemplate.getForEntity(baseUrl + "?page=1&size=10", ProductDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    ProductDTO[] products = response.getBody();
    assertNotNull(products);
    assertEquals(2, products.length);
  }

  @Disabled
  @Test
  void testUpdateProduct() {
    ProductDTO newProduct = new ProductDTO(null, "Mouse", "250");
    ProductDTO created = restTemplate.postForEntity(baseUrl + "/save", newProduct, ProductDTO.class).getBody();

    created.setName("Wireless Mouse");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ProductDTO> entity = new HttpEntity<>(created, headers);

    ResponseEntity<ProductDTO> updateResponse = restTemplate.exchange(baseUrl + "/" + created.getId(), HttpMethod.PUT, entity, ProductDTO.class);

    assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
    assertEquals("Wireless Mouse", updateResponse.getBody().getName());
  }

  @Test
  void testDeleteProduct() {
    ProductDTO newProduct = new ProductDTO(null, "Keyboard", "400");
    ProductDTO created = restTemplate.postForEntity(baseUrl+"/save", newProduct, ProductDTO.class).getBody();

    restTemplate.delete(baseUrl + "/" + created.getId());
  }
}
