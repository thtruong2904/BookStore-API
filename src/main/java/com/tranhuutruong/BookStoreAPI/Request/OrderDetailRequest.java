package com.tranhuutruong.BookStoreAPI.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailRequest {
    private Long product_id;

    private Long amount;
}
