package com.tranhuutruong.BookStoreAPI.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "order_detail", uniqueConstraints =@UniqueConstraint(columnNames = {"order_id", "product_detail_id"}))
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetailModel productDetailModel;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "price")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderModel orderModel;
}
