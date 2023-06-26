package com.tranhuutruong.BookStoreAPI.Repository.User;

import com.tranhuutruong.BookStoreAPI.Model.User.CustomerPointModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPointRepository extends JpaRepository<CustomerPointModel, Long> {
    @Query("SELECT c FROM CustomerPointModel c WHERE c.userInfoModel.accountModel.username = :username")
    CustomerPointModel getCustomerPointByUsername(@Param("username") String username);
}
