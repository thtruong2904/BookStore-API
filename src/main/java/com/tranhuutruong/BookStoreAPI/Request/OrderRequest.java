package com.tranhuutruong.BookStoreAPI.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderRequest {
    private String note;
    private Long feeShip;
    private String phone;
    private String address;
    private List<OrderDetailRequest> listProduct;

}
