package com.tranhuutruong.BookStoreAPI.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;
import com.tranhuutruong.BookStoreAPI.Request.User.LoginRequest;
import com.tranhuutruong.BookStoreAPI.Request.User.UserRegisterRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Response.User.LoginResponse;
import com.tranhuutruong.BookStoreAPI.Response.User.UserRegisterResponse;
import com.tranhuutruong.BookStoreAPI.Service.User.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Mono<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest)
    {
        LoginResponse loginResponse = userService.login(loginRequest);
        return Mono.just(ApiResponse.of(loginResponse));
    }

    @PostMapping(value = "/register")
    public UserRegisterResponse registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) throws MessagingException {
        UserRegisterResponse userRegisterResponse = userService.registerUser(userRegisterRequest);
        return userRegisterResponse;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String token)
    {
        return userService.confirmUserAccount(modelAndView, token);
    }
}
