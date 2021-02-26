package com.devtech.dto.bucket;

import com.devtech.entity.Bucket;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BucketResponse {
    private Long id;
    private Long productId;
    private Long userId;
    private String productName;
    private String producer;
    private BigDecimal productPrice;
    private Integer count;

    public BucketResponse(Bucket bucket) {
        this.id = bucket.getId();
        this.productId = bucket.getProduct().getId();
        this.userId = bucket.getUser().getId();
        this.productName = bucket.getProduct().getProductName();
        this.producer = bucket.getProduct().getProducer();
        this.productPrice = bucket.getProduct().getPrice();
        this.count = bucket.getCount();
    }
}
