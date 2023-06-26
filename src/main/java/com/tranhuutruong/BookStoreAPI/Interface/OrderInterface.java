package com.tranhuutruong.BookStoreAPI.Interface;

import com.tranhuutruong.BookStoreAPI.Model.OrderModel;
import com.tranhuutruong.BookStoreAPI.Request.OrderRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

public interface OrderInterface {
    public ApiResponse<Object> getAllOrder();

    public ApiResponse<Object> order(String username, Long receive_id, OrderRequest orderRequest);

    public ModelAndView confirmOrder(ModelAndView modelAndView, Long order_id);

    public ApiResponse<Object> confirmPayOrder(Long idOrder);

    public ApiResponse<Object> getOrderByStatus(String username, String status_name);

    public ApiResponse<Object> cancelOrderByUser(String username, Long idOrder);

    public Page<OrderModel> searchOrder(Date from, Date to, String query, String status, Integer page, Integer size);
}
