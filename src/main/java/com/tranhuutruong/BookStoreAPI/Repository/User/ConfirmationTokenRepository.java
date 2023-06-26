package com.tranhuutruong.BookStoreAPI.Repository.User;

import com.tranhuutruong.BookStoreAPI.Model.User.ConfirmationTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenModel, Long> {

    @Query("SELECT c FROM ConfirmationTokenModel c WHERE c.confirmationToken = :confirmationToken")
    ConfirmationTokenModel findByconfirmationToken(@Param("confirmationToken") String confirmationToken);
}
