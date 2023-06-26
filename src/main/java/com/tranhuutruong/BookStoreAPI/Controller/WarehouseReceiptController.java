package com.tranhuutruong.BookStoreAPI.Controller;

import com.tranhuutruong.BookStoreAPI.Request.AddProductDetailRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.WarehouseReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/ware-house")
public class WarehouseReceiptController {

    @Autowired
    WarehouseReceiptService warehouseReceiptService;

    @GetMapping(value = "/history_receipt")
    public Mono<Iterable<Map<String,Object>>> getHistory(@RequestParam(name = "from",required = false,defaultValue = "2022-01-01") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
                                                         @RequestParam(name = "to",required = false )@DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {
        return Mono.just(warehouseReceiptService.getHistory(fromDate,toDate==null?new Date():toDate));
    }

    @PostMapping(value = "/import")
    public ApiResponse<Object> createReceipt(@RequestBody List<AddProductDetailRequest> receiptDetail) throws Exception
    {
        return warehouseReceiptService.importWarehouse(receiptDetail);
    }
}
