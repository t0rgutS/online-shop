package com.devtech.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductCURequest {
    private String productName;
    private String photoURL;
    private String producer;
    private BigDecimal price;
    private String description;
    private Boolean condition;
    private Boolean shipType;
    private Integer count;
    private String categoryName;
    private String userLogin;
}
