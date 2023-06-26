package com.tranhuutruong.BookStoreAPI.Interface.User;

import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;
import com.tranhuutruong.BookStoreAPI.Request.User.LoginRequest;
import com.tranhuutruong.BookStoreAPI.Request.User.UpdateUserRequest;
import com.tranhuutruong.BookStoreAPI.Request.User.UserRegisterRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Response.User.LoginResponse;
import com.tranhuutruong.BookStoreAPI.Response.User.UserRegisterResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.util.Map;

public interface UserInterface {
    public LoginResponse login(LoginRequest loginRequest);

    public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) throws MessagingException;

    public ModelAndView confirmUserAccount(ModelAndView modelAndView, String confirmationToken);

    public ApiResponse<Object> getProfile(String username);

    public ApiResponse<Object> updateProfile(String username, UpdateUserRequest updateUserRequest);

    public ApiResponse<Object> getAllCustomer();

    public ApiResponse<Object> unlockCustomer(Long id);

    public ApiResponse<Object> lockCustomer(Long id);
}
