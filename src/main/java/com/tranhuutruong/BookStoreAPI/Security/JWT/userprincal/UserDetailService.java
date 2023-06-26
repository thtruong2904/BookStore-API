package com.tranhuutruong.BookStoreAPI.Security.JWT.userprincal;

import com.tranhuutruong.BookStoreAPI.Model.User.AccountModel;
import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;
import com.tranhuutruong.BookStoreAPI.Repository.User.AccountRepository;
import com.tranhuutruong.BookStoreAPI.Repository.User.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        if(accountModel == null || accountModel.getId() <= 0)
        {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản" + username);
        }
        UserInfoModel userInfoModel = userInfoRepository.findUserInfoModelByAccountModel(accountModel);

        return UserPrinciple.build(userInfoModel);
    }
}
