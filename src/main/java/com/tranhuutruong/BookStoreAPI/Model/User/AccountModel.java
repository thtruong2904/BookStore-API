package com.tranhuutruong.BookStoreAPI.Model.User;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "username", unique = true)
    private String username;

    @NonNull
    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleModel roleModel;

    @Column(name = "isActivity")
    private boolean isActivity;

    @Column(name = "verification_code")
    private String verificationCode;
}
