package com.tranhuutruong.BookStoreAPI.Request.User;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstname;

    private String lastname;

    private String phone;

    private String address;
}
