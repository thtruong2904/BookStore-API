package com.tranhuutruong.BookStoreAPI.Repository;

import com.tranhuutruong.BookStoreAPI.Model.ProductDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailModel, Long> {
    @Query("FROM ProductDetailModel pdm WHERE pdm.productModel.id = :id")
    Iterable<ProductDetailModel> findAllByProductModel_id(@Param("id") Long id);

    @Query("SELECT pdm FROM ProductDetailModel pdm WHERE pdm.productModel.id = :id AND pdm.size = :size AND pdm.color = :color")
    Optional<ProductDetailModel> findProductDetailModelByIdAndSizeAndColor(@Param("id") Long id, @Param("size") String size, @Param("color") String color);
}
