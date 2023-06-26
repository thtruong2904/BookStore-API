package com.tranhuutruong.BookStoreAPI.Response.User;

import lombok.Builder;
import lombok.Data;
import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;

@Builder
@Data
public class LoginResponse {
    private boolean status;

    private String message;

    private String accessToken;

    private Long expiresIn;

    private String refreshToken;

    private UserInfoModel userInfoModel;
}
