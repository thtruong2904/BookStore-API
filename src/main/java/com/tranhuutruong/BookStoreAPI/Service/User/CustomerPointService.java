package com.tranhuutruong.BookStoreAPI.Service.User;

import com.tranhuutruong.BookStoreAPI.Interface.User.CustomerPointInterface;
import com.tranhuutruong.BookStoreAPI.Model.User.CustomerPointModel;
import com.tranhuutruong.BookStoreAPI.Repository.User.CustomerPointRepository;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerPointService implements CustomerPointInterface {

    @Autowired
    private CustomerPointRepository customerPointRepository;

    @Override
    public ApiResponse<Object> getCustomerPoint(String username) {
        CustomerPointModel customerPointModel = customerPointRepository.getCustomerPointByUsername(username);
        if(customerPointModel == null || customerPointModel.getId() <= 0)
        {
            return ApiResponse.builder().status(101).message("Không tìm thấy thẻ tích điểm").build();
        }
        return ApiResponse.builder().status(200).message("Thẻ tích điểm").data(customerPointModel).build();
    }

    @Override
    public ApiResponse<Object> getLevelCustomer(String username) {
        CustomerPointModel customerPointModel = customerPointRepository.getCustomerPointByUsername(username);
        if(customerPointModel == null || customerPointModel.getId() <= 0)
        {
            return ApiResponse.builder().status(101).message("Không tìm thấy thẻ tích điểm").build();
        }
        else {
            if(customerPointModel.getPoints() < 50)
            {
                return ApiResponse.builder().message("Đồng").build();
            }
            else if(customerPointModel.getPoints() >= 50 && customerPointModel.getPoints() < 200)
            {
                return ApiResponse.builder().message("Bạc").build();
            }
            else if(customerPointModel.getPoints() >= 200 && customerPointModel.getPoints() < 500)
            {
                return ApiResponse.builder().message("Vàng").build();
            }
            else
            {
                return ApiResponse.builder().message("Bạch kim").build();
            }
        }
    }
}
