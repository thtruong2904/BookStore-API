package com.tranhuutruong.BookStoreAPI.Controller.User;

import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;
import com.tranhuutruong.BookStoreAPI.Request.User.UpdateUserRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/profile")
    public ApiResponse<Object> getProfileUser(Principal principal)
    {
        return userService.getProfile(principal.getName());
    }

    @PutMapping(value = "/profile")
    public ApiResponse<Object> updateProfile(Principal principal, @RequestBody UpdateUserRequest updateUserRequest)
    {
        return userService.updateProfile(principal.getName(), updateUserRequest);
    }

    @GetMapping(value = "/all")
    public ApiResponse<Object> getAllCustomer()
    {
        return userService.getAllCustomer();
    }

    @PutMapping(value = "/lock/{id}")
    public ApiResponse<Object> lockCustomer(@PathVariable("id") Long id)
    {
        return userService.lockCustomer(id);
    }

    @PutMapping(value = "/unlock/{id}")
    public ApiResponse<Object> unlockCustomer(@PathVariable("id") Long id)
    {
        return userService.unlockCustomer(id);
    }
}
