package com.problem.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;

    @NotNull(message = "Product name is required")
    @Size(min = 3, max = 50, message = "Product name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Product Price is required")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Price must be numeric and cannot contain spaces.")
    @Size(min = 1, max = 5, message = "Product name must be between 3 and 50 characters")
    private String price;
}
