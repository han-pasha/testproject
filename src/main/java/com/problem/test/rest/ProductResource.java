package com.problem.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.problem.test.dto.ProductDTO;
import com.problem.test.model.Product;
import com.problem.test.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product Management", description = "API for managing products")
public class ProductResource {

  @Context
  private UriInfo uriInfo;

  @Inject
  private ProductService productService;

  public UriInfo getUriInfo() {
    return uriInfo;
  }

  public void setUriInfo(UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  @GET
  @Operation(
      summary = "Get paginated list of products",
      description = "Returns a paginated list of products with default page size 10",
      responses = {
          @ApiResponse(responseCode = "200", description = "Successful operation",
              content = @Content(schema = @Schema(implementation = ProductDTO.class))),
          @ApiResponse(responseCode = "400", description = "Invalid parameters")
      }
  )
  public Response getAllProducts(
      @Parameter(description = "Page number (0-based)", example = "0")
      @QueryParam("page") @DefaultValue("0") @Min(0) int page,

      @Parameter(description = "Number of items per page", example = "10")
      @QueryParam("size") @DefaultValue("10") @Min(1) int size) {
    List<ProductDTO> products = productService.findPaginated(page, size);
    return Response.ok(products)
               .header("X-Total-Count", String.valueOf(products.size()))
               .build();
  }

  @GET
  @Path("/{id}")
  @Operation(
      summary = "Get product by ID",
      responses = {
          @ApiResponse(responseCode = "200", description = "Product found",
              content = @Content(schema = @Schema(implementation = Product.class))),
          @ApiResponse(responseCode = "404", description = "Product not found")
      }
  )
  public Response getProductById(
      @Parameter(description = "ID of the product to fetch", required = true, example = "1")
      @PathParam("id") Long id) {
    ProductDTO productDto = productService.getProductById(id);
    if (Objects.isNull(productDto)) return Response.status(Response.Status.NOT_FOUND).build();
    return Response.ok(productDto).build();
  }

  @POST
  @Path("/save")
  @Operation(
      summary = "Create a new product",
      responses = {
          @ApiResponse(responseCode = "201", description = "Product created",
              content = @Content(schema = @Schema(implementation = ProductDTO.class))),
          @ApiResponse(responseCode = "400", description = "Invalid input")
      }
  )
  public Response createProduct(
      @Parameter(description = "Product object to be created", required = true)
      @Valid ProductDTO productDto) {
    ProductDTO savedProduct = productService.saveProduct(productDto);
    URI location = uriInfo.getAbsolutePathBuilder()
                       .path(String.valueOf(savedProduct.getId()))
                       .build();
    return Response.created(location).entity(savedProduct).build();
  }

  @PUT
  @Path("/{id}")
  @Operation(
      summary = "Update an existing product",
      responses = {
          @ApiResponse(responseCode = "200", description = "Product updated",
              content = @Content(schema = @Schema(implementation = Product.class))),
          @ApiResponse(responseCode = "400", description = "Invalid input"),
          @ApiResponse(responseCode = "404", description = "Product not found")
      }
  )
  public Response updateProduct(
      @Parameter(description = "ID of the product to update", required = true, example = "1")
      @PathParam("id") Long id,
      @Parameter(description = "Updated product object", required = true)
      @Valid ProductDTO productDto) {
    productDto.setId(id); // Ensure ID consistency
    productDto = productService.updateProduct(productDto);
    if (Objects.isNull(productDto)) return Response.status(Response.Status.NOT_FOUND).build();
    return Response.ok(productDto).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(
      summary = "Delete a product",
      responses = {
          @ApiResponse(responseCode = "204", description = "Product deleted"),
          @ApiResponse(responseCode = "404", description = "Product not found")
      }
  )
  public Response deleteProduct(
      @Parameter(description = "ID of the product to delete", required = true, example = "1")
      @PathParam("id") Long id) {
    productService.deleteProduct(id);
    return Response.noContent().build();
  }
}