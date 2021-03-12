package com.devtech.dto.cartwish;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartWishCURequest {
    private String login;
    private Long productId;
    private Integer count;
}
