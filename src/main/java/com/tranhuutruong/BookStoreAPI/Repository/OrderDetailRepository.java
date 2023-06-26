package com.tranhuutruong.BookStoreAPI.Repository;

import com.tranhuutruong.BookStoreAPI.Model.OrderDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailModel, Long> {


}
