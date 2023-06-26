package com.tranhuutruong.BookStoreAPI.Controller;

import com.tranhuutruong.BookStoreAPI.Request.OrderRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/all")
    public ApiResponse<Object> getAllOrder()
    {
        return orderService.getAllOrder();
    }

    @PostMapping(value = "/create/{receive_id}")
    public ApiResponse<Object> createOrder(Principal principal, @PathVariable Long receive_id, @RequestBody OrderRequest orderRequest)
    {
        return orderService.order(principal.getName(), receive_id, orderRequest);
    }

    @RequestMapping(value = "/confirm-order", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmOrder(ModelAndView modelAndView, @RequestParam("id-order") Long idOrder)
    {
        return orderService.confirmOrder(modelAndView, idOrder);
    }

    @GetMapping(value = "/status={statusName}")
    public ApiResponse<Object> getAllByStatusName(Principal principal, @PathVariable("statusName") String statusName)
    {
        return orderService.getOrderByStatus(principal.getName(), statusName);
    }

    @DeleteMapping(value = "/cancel_order")
    public ApiResponse<Object> cancelOderByUser(Principal principal, @RequestParam("order_id") Long orderId)
    {
        return orderService.cancelOrderByUser(principal.getName(), orderId);
    }

    @PostMapping(value = "/invoice/{idOrder}")
    public ApiResponse<Object> sendInvoice(Principal principal, @PathVariable("idOrder") Long idOrder)
    {
        return orderService.electronicInvoice(principal.getName(), idOrder);
    }
}
