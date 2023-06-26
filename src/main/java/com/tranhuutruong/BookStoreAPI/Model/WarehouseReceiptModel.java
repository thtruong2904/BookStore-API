package com.tranhuutruong.BookStoreAPI.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "warehouse_receipt")
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceiptModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createdDate")
    private String createdDate;
}
