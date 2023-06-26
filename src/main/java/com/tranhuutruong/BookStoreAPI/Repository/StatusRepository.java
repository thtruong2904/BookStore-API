package com.tranhuutruong.BookStoreAPI.Repository;

import com.tranhuutruong.BookStoreAPI.Model.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<StatusModel, Long> {
    @Query("SELECT s FROM StatusModel s WHERE s.id = :id")
    Optional<StatusModel> findById(@Param("id") Long id);

    @Query("SELECT s FROM StatusModel s WHERE s.name = :name")
    StatusModel findByName(@Param("name") String name);
}
