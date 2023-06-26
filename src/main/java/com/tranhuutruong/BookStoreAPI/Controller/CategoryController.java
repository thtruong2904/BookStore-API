package com.tranhuutruong.BookStoreAPI.Controller;

import com.tranhuutruong.BookStoreAPI.Request.CategoryRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/all")
    public ApiResponse<Object> getAllCategory()
    {
        return categoryService.getAllCategory();
    }

    @PostMapping(value = "/add")
    public ApiResponse<Object> addCategory(@RequestBody CategoryRequest categoryRequest)
    {
        return categoryService.addCategory(categoryRequest);
    }

    @PutMapping(value = "/update/{id}")
    public ApiResponse<Object> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryRequest categoryRequest)
    {
        return categoryService.updateCategory(id, categoryRequest);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiResponse<Object> deleteCategory(@PathVariable("id") Long id)
    {
        return categoryService.deleteCategory(id);
    }
}
