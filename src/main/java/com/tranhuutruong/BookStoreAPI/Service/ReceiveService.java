package com.tranhuutruong.BookStoreAPI.Service;

import com.tranhuutruong.BookStoreAPI.Interface.ReceiveInterface;
import com.tranhuutruong.BookStoreAPI.Model.ReceiveModel;
import com.tranhuutruong.BookStoreAPI.Repository.ReceiveRepository;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiveService implements ReceiveInterface {

    @Autowired
    private ReceiveRepository receiveRepository;

    @Override
    public ApiResponse<Object> getAllReceive() {
        List<ReceiveModel> receiveModels = receiveRepository.findAll();
        return ApiResponse.builder().status(200).data(receiveModels).build();
    }
}
