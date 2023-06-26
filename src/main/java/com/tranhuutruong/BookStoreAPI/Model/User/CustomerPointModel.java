package com.tranhuutruong.BookStoreAPI.Model.User;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "customer_points")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userinfo_id")
    @JsonBackReference
    private UserInfoModel userInfoModel;

    @Column(name = "points")
    private Long points;

    public CustomerPointModel(UserInfoModel userInfoModel)
    {
        this.userInfoModel = userInfoModel;
        points = 0L;
    }
}
