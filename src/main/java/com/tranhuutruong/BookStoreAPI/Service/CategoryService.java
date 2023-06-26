package com.tranhuutruong.BookStoreAPI.Service;

import com.tranhuutruong.BookStoreAPI.Model.CategoryModel;
import com.tranhuutruong.BookStoreAPI.Model.ProductModel;
import com.tranhuutruong.BookStoreAPI.Repository.ProductRepository;
import com.tranhuutruong.BookStoreAPI.Request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranhuutruong.BookStoreAPI.Interface.CategoryInterface;
import com.tranhuutruong.BookStoreAPI.Repository.CategoryRepository;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryInterface {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ApiResponse<Object> getAllCategory()
    {
        List<CategoryModel> list = categoryRepository.findAll();
        return ApiResponse.builder().status(200).message("Danh sách danh mục").data(list).build();
    }

    @Override
    public ApiResponse<Object> addCategory(CategoryRequest categoryRequest)
    {
        CategoryModel categoryModel = categoryRepository.findByName(categoryRequest.getName());
        if(categoryModel != null)
        {
            return ApiResponse.builder().status(101).message("Danh mục không tồn tại!").build();
        }
        CategoryModel categoryModel1 = CategoryModel.builder().name(categoryRequest.getName()).build();
        categoryRepository.save(categoryModel1);
        return ApiResponse.builder().status(200).message("Thêm danh mục thành công!").build();
    }

    @Override
    public ApiResponse<Object> updateCategory(Long id, CategoryRequest categoryRequest)
    {
        Optional<CategoryModel> categoryModel = categoryRepository.findById(id);
        if(categoryModel.isPresent())
        {
            categoryModel.get().setName(categoryRequest.getName());
            categoryRepository.save(categoryModel.get());
            return ApiResponse.builder().status(200).message("Cập nhật danh mục thành công!").build();
        }
        else {
            return ApiResponse.builder().status(101).message("Danh mục không tồn tại!").build();
        }
    }

    @Override
    public ApiResponse<Object> deleteCategory(Long id)
    {
        Optional<CategoryModel> categoryModel = categoryRepository.findById(id);
        if(categoryModel.isPresent())
        {
            Iterable<ProductModel> iterable = productRepository.findAllByCategoryModel_Name(categoryModel.get().getName());
            ArrayList<ProductModel> list = new ArrayList<>();
            iterable.forEach(list::add);
            if(list.size() > 0)
            {
                return ApiResponse.builder().status(101).message("Danh mục có sản phẩm. Không thể xóa!").build();
            }
            categoryRepository.delete(categoryModel.get());
            return ApiResponse.builder().status(200).message("Xóa danh mục thành công!").build();
        }
        else {
            return ApiResponse.builder().status(0).message("Danh mục không tồn tại!").build();
        }
    }


}
