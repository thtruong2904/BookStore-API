package com.tranhuutruong.BookStoreAPI.Service;

//import com.tranhuutruong.BookStoreAPI.Interface.ImageInterface;
import com.tranhuutruong.BookStoreAPI.Interface.ProductInterface;
import com.tranhuutruong.BookStoreAPI.Model.CategoryModel;
import com.tranhuutruong.BookStoreAPI.Model.ProductDetailModel;
import com.tranhuutruong.BookStoreAPI.Model.ProductModel;
import com.tranhuutruong.BookStoreAPI.Repository.CategoryRepository;
import com.tranhuutruong.BookStoreAPI.Repository.ProductDetailRepository;
import com.tranhuutruong.BookStoreAPI.Repository.ProductRepository;
import com.tranhuutruong.BookStoreAPI.Request.ProductRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService implements ProductInterface {

    @Autowired
    ProductRepository productRepository;

//    @Autowired
//    ImageInterface imageInterface;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public ApiResponse<Object> getAllProduct()
    {
        Iterable<ProductModel> allProduct = productRepository.findAllSortCategory();
        return ApiResponse.builder().message("All Product").status(200).data(allProduct).build();
    }

    @Override
    public ApiResponse<Object> getBestSellerProduct()
    {
        Iterable<ProductModel> bestSellerProduct = productRepository.getProductBestSeller();
        return ApiResponse.builder().message("All Product Best Seller").status(200).data(bestSellerProduct).build();
    }

    @Override
    public ApiResponse<Object> createProduct(ProductRequest productRequest, FilePart filePart) throws IOException{
        CategoryModel categoryModel = categoryRepository.findByName(productRequest.getCategory());
        if(categoryModel == null)
        {
            return ApiResponse.builder().message("Không tìm thấy danh mục").status(100).build();
        }
        if(productRepository.existsByName(productRequest.getName()))
        {
            ApiResponse.builder().message("Tên sản phẩm đã tồn tại").status(100).build();
        }
        ProductModel newProduct = ProductModel.builder().name(productRequest.getName())
                .categoryModel(categoryModel)
                .description(productRequest.getDescription())
//                .linkImage(imageInterface.save(filePart))
                .price(productRequest.getPrice())
                .sold(0L).isDelete(false).author(productRequest.getAuthor()).build();

        productRepository.save(newProduct);
        return ApiResponse.builder().message("Tạo sản phẩm thành công").status(200).data(newProduct).build();
    }
    @Override
    public ApiResponse<Object> getProductById(Long id)
    {
        Optional<ProductModel> productModel = productRepository.findById(id);
        if(productModel.isPresent())
        {
            return ApiResponse.builder().message("Chi tiết sản phẩm").status(200).data(productModel.get()).build();
        }
        return ApiResponse.builder().message("Không tìm thấy sản phẩm").status(101).build();
    }

    @Override
    public ApiResponse<Object> getDetailProductById(Long id)
    {
        Iterable<ProductDetailModel> detailProduct = productDetailRepository.findAllByProductModel_id(id);
        return ApiResponse.builder().message("Product Detail").status(200).data(detailProduct).build();
    }

    @Override
    public ApiResponse<Object> getAllByCategoryName(String categoryName)
    {
        Iterable<ProductModel> list = productRepository.findAllByCategoryModel_Name(categoryName);
        return ApiResponse.builder().message("Sản phầm theo danh mục").status(200).data(list).build();
    }

    @Override
    public ApiResponse<Object> searchProductByName(String name)
    {
        Iterable<ProductModel> list = productRepository.searchProductByName(name);
        return ApiResponse.builder().message("Sản phẩm theo tên").status(200).data(list).build();
    }

    @Override
    public Page<Map<String, Object>> getPagingProduct(Integer page, Integer size)
    {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("categoryModel.name").ascending());
        return productRepository.getListProductPaging(pageable);
    }
}
