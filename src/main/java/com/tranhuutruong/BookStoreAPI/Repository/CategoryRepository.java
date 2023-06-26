package com.tranhuutruong.BookStoreAPI.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tranhuutruong.BookStoreAPI.Model.CategoryModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
    List<CategoryModel> findAll();

    @Query("FROM CategoryModel c WHERE c.name = :name")
    CategoryModel findByName(@Param("name") String name);

    @Query("FROM CategoryModel c WHERE c.id = :id")
    Optional<CategoryModel> findById(@Param("id") Long id);
}
