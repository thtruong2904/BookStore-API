package com.tranhuutruong.BookStoreAPI.Repository;

import com.tranhuutruong.BookStoreAPI.Model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {

    @Query("SELECT o FROM OrderModel o WHERE o.userInfoModel.accountModel.username = :username")
    Iterable<OrderModel> findAllOrderByUsername(@Param("username") String username);

    @Query("SELECT o FROM OrderModel o WHERE o.userInfoModel.accountModel.username = :username AND o.statusModel.name = :status_name")
    Iterable<OrderModel> findAllByNameStatus(@Param("username") String username, @Param("status_name") String status_name);

    @Query("SELECT o FROM OrderModel o WHERE o.userInfoModel.accountModel.username = :username AND o.id = :id")
    OrderModel findBOrderByUserNameAndId(@Param("username") String username, @Param("id") Long idOrder);

    @Query(" SELECT u FROM OrderModel  u " +
            "where (:from is NULL  OR :to is NULL  OR u.date BETWEEN :from AND :to)" +
            "and (:status is NULL  OR u.statusModel.name like %:status%)" +
            "and (u.date is not null ) " +
            "and (:info is null OR u.userInfoModel.phone like %:info% OR u.receiveModel.name like %:info% )"
    )
//    Page<OrderModel> searchOrder(@Param("from") Instant from, @Param("to") Instant to, @Param("info") String info, @Param("status") String status, Pageable pageable);
    Page<OrderModel> searchOrder(@Param("from") String from, @Param("to") String to, @Param("info") String info, @Param("status") String status, Pageable pageable);
}