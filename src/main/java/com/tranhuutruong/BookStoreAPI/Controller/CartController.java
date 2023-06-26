package com.tranhuutruong.BookStoreAPI.Controller;

import com.tranhuutruong.BookStoreAPI.Request.CartRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.CartService;
import org.hibernate.hql.internal.classic.AbstractParameterInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping(value = "/addToCart")
    public ApiResponse<Object> addToCart(Principal principal, @RequestBody CartRequest cartRequest)
    {
        return cartService.addToCart(principal.getName(), cartRequest);
    }

    @PutMapping(value = "/update")
    public ApiResponse<Object> updateCart(Principal principal, @RequestBody CartRequest cartRequest)
    {
        return cartService.updateCart(principal.getName(), cartRequest);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiResponse<Object> updateCart(Principal principal, @PathVariable("id") Long productId)
    {
        return cartService.deleteItem(principal.getName(), productId);
    }
    @GetMapping(value = "/all")
    public ApiResponse<Object> getAllCart(Principal principal)
    {
        return cartService.getAllByUser(principal.getName());
    }

}
