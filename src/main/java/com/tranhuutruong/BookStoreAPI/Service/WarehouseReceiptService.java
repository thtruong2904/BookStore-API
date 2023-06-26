package com.tranhuutruong.BookStoreAPI.Service;

import com.tranhuutruong.BookStoreAPI.Interface.WarehouseReceiptInterface;
import com.tranhuutruong.BookStoreAPI.Model.ProductDetailModel;
import com.tranhuutruong.BookStoreAPI.Model.ProductModel;
import com.tranhuutruong.BookStoreAPI.Model.WarehouseReceiptDetailModel;
import com.tranhuutruong.BookStoreAPI.Model.WarehouseReceiptModel;
import com.tranhuutruong.BookStoreAPI.Repository.ProductDetailRepository;
import com.tranhuutruong.BookStoreAPI.Repository.ProductRepository;
import com.tranhuutruong.BookStoreAPI.Repository.WarehouseReceiptDetailRepository;
import com.tranhuutruong.BookStoreAPI.Repository.WarehouseReceiptRepository;
import com.tranhuutruong.BookStoreAPI.Request.AddProductDetailRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Utils.CurrentDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WarehouseReceiptService implements WarehouseReceiptInterface {

    @Autowired
    WarehouseReceiptRepository warehouseReceiptRepository;

    @Autowired
    WarehouseReceiptDetailRepository warehouseReceiptDetailRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Override
    public Iterable<Map<String, Object>> getHistory(Date fromDate, Date toDate)
    {
        return warehouseReceiptRepository.getAllReceipt(fromDate.toInstant(), toDate.toInstant());
    }

    @Override
    public ApiResponse<Object> importWarehouse(List<AddProductDetailRequest> listProductDetail)
    {
        String message = "";
        int status = 101;
        try{
            WarehouseReceiptModel newWarehouseReceiptModel = WarehouseReceiptModel.builder().createdDate(CurrentDateTime.getCurrentDateTime()).build();
            warehouseReceiptRepository.save(newWarehouseReceiptModel);
            for (AddProductDetailRequest productDetailRequest : listProductDetail)
            {
                Optional<ProductModel> productModel = productRepository.findById(productDetailRequest.getProduct_id());
                if(!productModel.isPresent())
                {
                    throw new Exception("Không tìm thấy sản phẩm cần thêm");
                }
                Optional<ProductDetailModel> productDetailModel = productDetailRepository.findProductDetailModelByIdAndSizeAndColor(productDetailRequest.getProduct_id(), productDetailRequest.getSize(), productDetailRequest.getColor());
                ProductDetailModel tmp = null;
                if(productDetailModel.isPresent()) // nếu đã tồn tại sản phẩm với size và color
                {
                    productDetailModel.get().setCurrent_number(productDetailModel.get().getCurrent_number() + productDetailRequest.getNumberAdd());
                    productDetailRepository.save(productDetailModel.get());
                    tmp = productDetailModel.get();
                }
                else{ // nếu sản phẩm chưa có size và color thì tạo chi tiết sản phẩm
                    ProductDetailModel newProductDetailModel1 = ProductDetailModel.builder()
                            .productModel(productModel.get()).size(productDetailRequest.getSize())
                            .color(productDetailRequest.getColor()).current_number(productDetailRequest.getNumberAdd()).build();
                    productDetailRepository.save(newProductDetailModel1);
                    tmp = newProductDetailModel1;
                }
                WarehouseReceiptDetailModel newReceipt = WarehouseReceiptDetailModel.builder()
                        .warehouseReceiptModel(newWarehouseReceiptModel).productDetailModel(tmp)
                        .amount(productDetailRequest.getNumberAdd())
                        .prices(productDetailRequest.getPrices()).build();
                warehouseReceiptDetailRepository.save(newReceipt);
            }
            message = "Nhập hàng thành công!";
            status = 200;
        }
        catch (Exception e)
        {
            message = "Lỗi nhập hàng !" + e.getMessage();
        }
        return ApiResponse.builder().message(message).status(status).build();
    }
}
