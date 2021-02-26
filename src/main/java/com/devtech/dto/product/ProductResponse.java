package com.devtech.dto.product;

import com.devtech.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private Long id;
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
    private Integer rating = 0;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.photoURL = product.getPhotoURL();
        this.producer = product.getProducer();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.condition = product.getCondition();
        this.shipType = product.getShipType();
        this.count = product.getCount();
        this.categoryName = product.getCategory().getCategoryName();
        this.userLogin = product.getUser().getLogin();
    }

    public ProductResponse(Product product, Integer rating) {
        this(product);
        this.rating = rating;
    }
}
