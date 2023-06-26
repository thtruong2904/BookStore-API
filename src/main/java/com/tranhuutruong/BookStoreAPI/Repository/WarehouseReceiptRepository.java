package com.tranhuutruong.BookStoreAPI.Repository;

import com.tranhuutruong.BookStoreAPI.Model.WarehouseReceiptModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;

@Repository
public interface WarehouseReceiptRepository extends JpaRepository<WarehouseReceiptModel, Long> {
    @Query("SELECT u.id as id, u.createdDate as createdDate, sum(k.amount) as totalAmount, " +
            " sum(k.amount*k.prices) as totalMoney " +
            "FROM WarehouseReceiptModel u left join WarehouseReceiptDetailModel k " +
            "on u.id=k.warehouseReceiptModel.id" +
            " group by u.id,u.createdDate")
    Iterable<Map<String, Object>> getAllReceipt(Instant from, Instant to);
}
