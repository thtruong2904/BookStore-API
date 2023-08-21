package com.tranhuutruong.BookStoreAPI.Controller;

import com.tranhuutruong.BookStoreAPI.Request.ProductRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ApiResponse<Object> getAllProduct()
    {
        return productService.getAllProduct();
    }

    @GetMapping("/best-seller")
    public ApiResponse<Object> getBestSeller()
    {
        return productService.getBestSellerProduct();
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getProductById(@PathVariable Long id)
    {
        return productService.getProductById(id);
    }
    @GetMapping(value = "/detail/{id_product}")
    public ApiResponse<Object> getDetailProductById(@PathVariable Long id_product)
    {
        return productService.getDetailProductById(id_product);
    }

    @GetMapping(value = "/category={categoryName}")
    public ApiResponse<Object> getAllByCategory(@PathVariable String categoryName)
    {
        return productService.getAllByCategoryName(categoryName);
    }

    @GetMapping(value = "/search")
    public ApiResponse<Object> searchByName(@RequestParam(required = false) String name)
    {
        return productService.searchProductByName(name);
    }

    @GetMapping(value = "/get_paging")
    public Mono<Page<Map<String, Object>>> getPaging(@RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10", required = false) Integer size)
    {
        return Mono.just(productService.getPagingProduct(page,size));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Object> createProduct(@RequestParam("name") String name,
                                             @RequestParam("category") String category,
                                             @RequestParam("description") String description,
                                             @RequestParam("price") Long price,
                                             @RequestParam("author") String author,
                                             @RequestPart("image") FilePart filePart) throws IOException{
        ProductRequest productRequest = ProductRequest.builder().name(name)
                .category(category).description(description).price(price)
                .author(author).image(filePart).build();
        return productService.createProduct(productRequest, filePart);
    }
}
