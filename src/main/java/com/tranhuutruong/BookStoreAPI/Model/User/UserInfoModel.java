package com.tranhuutruong.BookStoreAPI.Model.User;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", columnDefinition = "nvarchar(255)")
    private String firstname;

    @Column(name = "lastname", columnDefinition = "nvarchar(255)")
    private String lastname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountModel accountModel;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "userInfoModel")
    @JsonManagedReference
    private CustomerPointModel customerPointModel;
}
