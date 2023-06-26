package com.tranhuutruong.BookStoreAPI.Interface;

import com.tranhuutruong.BookStoreAPI.Request.CategoryRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;

public interface CategoryInterface {
    public ApiResponse<Object> getAllCategory();

    public ApiResponse<Object> addCategory(CategoryRequest categoryRequest);

    public ApiResponse<Object> updateCategory(Long id, CategoryRequest categoryRequest);

    public ApiResponse<Object> deleteCategory(Long id);


}
