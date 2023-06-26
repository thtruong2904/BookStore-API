package com.tranhuutruong.BookStoreAPI.Request.User;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class UserRegisterRequest {
    private String userName;
    private String passWord;
    private String firstName;
    private String lastName;

    @NotNull(message = "Email not null!")
    @Email(message = "Wrong email format!")
    private String email;

    @NotNull(message = "Phone not null")
    private String phone;

    private String roleName;
}
