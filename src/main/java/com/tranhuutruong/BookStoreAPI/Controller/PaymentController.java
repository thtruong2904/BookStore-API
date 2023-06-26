package com.tranhuutruong.BookStoreAPI.Controller;

import com.tranhuutruong.BookStoreAPI.Model.PaymentModel;
import com.tranhuutruong.BookStoreAPI.Service.PaymentService;
import com.tranhuutruong.BookStoreAPI.VNPay.VNPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;
    @GetMapping(value = "/create_payment/{idOrder}")
    public ResponseEntity<?> createPayment(@PathVariable("idOrder") Long idOrder) throws UnsupportedEncodingException{
        return paymentService.createPayment(idOrder);
    }

}
