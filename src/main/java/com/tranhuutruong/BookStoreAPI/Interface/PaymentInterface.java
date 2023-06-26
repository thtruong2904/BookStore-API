package com.tranhuutruong.BookStoreAPI.Interface;

import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

public interface PaymentInterface {
    public ResponseEntity<?> createPayment(Long idOrder) throws UnsupportedEncodingException;
}
