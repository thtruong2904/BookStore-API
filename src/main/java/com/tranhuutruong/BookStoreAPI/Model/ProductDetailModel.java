package com.tranhuutruong.BookStoreAPI.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "product_detail")
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product")
    private ProductModel productModel;

    @Column(name = "size")
    private String size;

    @Column(name = "color")
    private String color;

    @Column(name = "current_number")
    private Long current_number;
}
