package com.tranhuutruong.BookStoreAPI.Model;

import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "cart", uniqueConstraints =@UniqueConstraint(columnNames = {"userinfo_id", "product_detail_id"}))
@AllArgsConstructor
@NoArgsConstructor
public class CartModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userinfo_id")
    private UserInfoModel userInfoModel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_detail_id")
    private ProductDetailModel productDetailModel;

    @Column(name = "amount")
    private Long amount;
}
