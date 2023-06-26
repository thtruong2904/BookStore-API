package com.tranhuutruong.BookStoreAPI.Repository.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tranhuutruong.BookStoreAPI.Model.User.AccountModel;
import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;

import java.util.List;
import java.util.Map;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoModel, Long> {
    @Query("SELECT u FROM UserInfoModel u WHERE u.accountModel=:account")
    UserInfoModel findUserInfoModelByAccountModel(@Param("account") AccountModel accountModel);

    @Query("SELECT u FROM UserInfoModel u WHERE u.accountModel.username = :username")
    UserInfoModel findUserInfoModelByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserInfoModel u WHERE u.accountModel.roleModel.name = 'CUSTOMER'")
    Iterable<UserInfoModel> getAllCustomer();
}
