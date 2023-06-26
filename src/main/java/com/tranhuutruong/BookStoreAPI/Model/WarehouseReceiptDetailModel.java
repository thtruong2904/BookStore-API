package com.tranhuutruong.BookStoreAPI.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "warehouse_receipt_detail")
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseReceiptDetailModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_receipt_id")
    private WarehouseReceiptModel warehouseReceiptModel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_detail_id")
    private ProductDetailModel productDetailModel;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "prices")
    private Integer prices;
}
