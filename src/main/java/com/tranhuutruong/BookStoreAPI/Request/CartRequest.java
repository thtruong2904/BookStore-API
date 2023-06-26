package com.tranhuutruong.BookStoreAPI.Request;

import lombok.Data;

@Data
public class CartRequest {
    private Long idProduct;

    private Long amount;
}
