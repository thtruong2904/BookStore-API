package com.tranhuutruong.BookStoreAPI.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tranhuutruong.BookStoreAPI.Model.User.AccountModel;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    @Query("SELECT u FROM AccountModel u WHERE u.username=:userName")
    AccountModel findAccountModelByUsername(@Param("userName") String userName);

    @Query("SELECT u FROM AccountModel u WHERE u.email=:email")
    AccountModel findAccountModelByEmail(@Param("email") String email);

    boolean existsByEmail( String email);
}
