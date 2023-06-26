package com.tranhuutruong.BookStoreAPI.Controller;

import com.tranhuutruong.BookStoreAPI.Model.OrderModel;
import com.tranhuutruong.BookStoreAPI.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/admin/order")
public class OrderAdminController {

    @Autowired
    OrderService orderService;

    @GetMapping(value = "/search")
    public Mono<Page<OrderModel>> getTheOrderByStatus(@RequestParam(name = "from", required = false, defaultValue = "2022-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                      @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                                      @RequestParam(name = "status", required = false) String status,
                                                      @RequestParam(name = "info", required = false) String info,
                                                      @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
                                                      @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) throws ParseException {
        return Mono.just(orderService.searchOrder(fromDate, toDate == null ? new Date() : toDate, info, status, page, size));
    }
}
