package com.tranhuutruong.BookStoreAPI.Repository;

import com.tranhuutruong.BookStoreAPI.Model.ReceiveModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiveRepository extends JpaRepository<ReceiveModel, Long> {

    @Query("SELECT r FROM ReceiveModel r WHERE r.id = :id")
    Optional<ReceiveModel> findById(@Param("id") Long id);

    @Query("SELECT r FROM ReceiveModel r WHERE r.name = :name")
    ReceiveModel findByName(@Param("name") String name);
}
