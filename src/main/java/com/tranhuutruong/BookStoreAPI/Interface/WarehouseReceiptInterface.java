package com.tranhuutruong.BookStoreAPI.Interface;

import com.tranhuutruong.BookStoreAPI.Request.AddProductDetailRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WarehouseReceiptInterface {
    public Iterable<Map<String, Object>> getHistory(Date fromDate, Date toDate);

    public ApiResponse<Object> importWarehouse(List<AddProductDetailRequest> listProductDetail);
}
