package com.tranhuutruong.BookStoreAPI.Controller.User;

import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.User.CustomerPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/customer-point")
public class CustomerPointController {

    @Autowired
    CustomerPointService customerPointService;

    @GetMapping()
    public ApiResponse<Object> getCustomerPoint(Principal principal)
    {
        return customerPointService.getCustomerPoint(principal.getName());
    }

    @GetMapping(value = "/level")
    public ApiResponse<Object> getLevelCustomer(Principal principal)
    {
        return customerPointService.getLevelCustomer(principal.getName());
    }
}
