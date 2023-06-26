package com.tranhuutruong.BookStoreAPI.Interface.User;

import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;

public interface CustomerPointInterface {

    public ApiResponse<Object> getCustomerPoint(String username);

    public ApiResponse<Object> getLevelCustomer(String username);
}
