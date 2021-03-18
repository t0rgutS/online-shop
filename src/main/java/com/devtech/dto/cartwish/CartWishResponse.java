package com.devtech.dto.cartwish;

import com.devtech.entity.CartWish;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartWishResponse {
    private Long id;
    private Long productId;
    private Long userId;
    private String productName;
    private String producer;
    private BigDecimal price;
    private Integer count;

    public CartWishResponse(CartWish cartWish) {
        this.id = cartWish.getId();
        this.productId = cartWish.getProduct().getId();
        this.userId = cartWish.getUser().getId();
        this.productName = cartWish.getProduct().getProductName();
        this.producer = cartWish.getProduct().getProducer().getProducerName();
        this.price = cartWish.getProduct().getPrice();
        this.count = cartWish.getCount();
    }
}
