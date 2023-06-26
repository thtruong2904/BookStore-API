package com.tranhuutruong.BookStoreAPI.Repository;

import com.tranhuutruong.BookStoreAPI.Model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    @Query("FROM ProductModel p where p.isDelete = false order by p.categoryModel.id ASC")
    Iterable<ProductModel> findAllSortCategory();

    @Query(value = "SELECT * FROM product ORDER BY sold DESC LIMIT 20", nativeQuery = true)
    Iterable<ProductModel> getProductBestSeller();

    @Query("from ProductModel p where p.name like %:name% and p.isDelete =false")
    Iterable<ProductModel> searchProductByName(@Param("name") String name);

    @Query("FROM ProductModel p WHERE p.categoryModel.name = :nameCategory AND p.isDelete = false")
    Iterable<ProductModel> findAllByCategoryModel_Name(@Param("nameCategory") String nameCategory);

    boolean existsByName(String name);

    @Query(" SELECT u.id as id, u.name as name,u.description as describe,u.categoryModel.name as category, u.isDelete as status ," +
            " u.linkImage as linkImg, u.price as price, u.sold as sold, sum(k.current_number) as quantityInStock" +
            " FROM ProductModel  u left join ProductDetailModel k on u.id = k.productModel.id" +
            " group by u.id,  u.name, u.description,u.categoryModel.name, " +
            " u.linkImage ,u.price, u.sold, u.isDelete"
    )
    Page<Map<String, Object>> getListProductPaging(Pageable pageable);
}
