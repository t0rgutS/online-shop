package com.devtech.dto.bucket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BucketCURequest {
    private String login;
    private Long productId;
    private Integer count;
}
