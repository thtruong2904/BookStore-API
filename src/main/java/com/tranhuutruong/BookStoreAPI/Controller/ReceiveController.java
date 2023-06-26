package com.tranhuutruong.BookStoreAPI.Controller;

import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/receive")
public class ReceiveController {

    @Autowired
    ReceiveService receiveService;

    @GetMapping(value = "/all")
    public ApiResponse<Object> getAllReceive()
    {
        return receiveService.getAllReceive();
    }
}
