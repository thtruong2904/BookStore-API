package com.tranhuutruong.BookStoreAPI.Interface;

import com.tranhuutruong.BookStoreAPI.Request.CartRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;

public interface CartInterface {
    public ApiResponse<Object> addToCart(String username, CartRequest cartRequest);

    public ApiResponse<Object> getAllByUser(String username);

    public ApiResponse<Object> updateCart(String username, CartRequest cartRequest);

    public ApiResponse<Object> deleteItem(String username, Long productId);
}
