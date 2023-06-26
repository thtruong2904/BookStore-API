package com.tranhuutruong.BookStoreAPI.Service;

import com.tranhuutruong.BookStoreAPI.Interface.OrderDetailInterface;
import com.tranhuutruong.BookStoreAPI.Model.OrderDetailModel;
import com.tranhuutruong.BookStoreAPI.Repository.OrderDetailRepository;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService implements OrderDetailInterface {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public ApiResponse<Object> getAllOrderDetail()
    {
        List<OrderDetailModel> list = orderDetailRepository.findAll();
        return ApiResponse.builder().status(200).data(list).build();
    }
}
