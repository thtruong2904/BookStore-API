package com.tranhuutruong.BookStoreAPI.Repository;

import com.tranhuutruong.BookStoreAPI.Model.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartModel, Long> {
    @Query("SELECT c FROM CartModel c WHERE c.userInfoModel.accountModel.username = :username AND c.productDetailModel.id = :productId")
    CartModel findCartModelByUserInfoModel_AccountModel_UsernameAndProductDetail_Id(@Param("username") String username, @Param("productId") Long productId);

    @Query("SELECT c FROM CartModel c WHERE c.userInfoModel.accountModel.username = :username")
    Iterable<CartModel> findAllCartMoDelByUserName(@Param("username") String username);
}
