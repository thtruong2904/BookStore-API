package com.tranhuutruong.BookStoreAPI.Interface;

import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ProductInterface {
    public ApiResponse<Object> getAllProduct();
    public ApiResponse<Object> getBestSellerProduct();
    public ApiResponse<Object> getProductById(Long id);
    public ApiResponse<Object> getDetailProductById(Long id);
    public ApiResponse<Object> getAllByCategoryName(String categoryName);
    public ApiResponse<Object> searchProductByName(String name);
    public Page<Map<String, Object>> getPagingProduct(Integer page, Integer size);
}
