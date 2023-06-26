package com.tranhuutruong.BookStoreAPI.Service;

import com.tranhuutruong.BookStoreAPI.Interface.CartInterface;
import com.tranhuutruong.BookStoreAPI.Model.CartModel;
import com.tranhuutruong.BookStoreAPI.Model.ProductDetailModel;
import com.tranhuutruong.BookStoreAPI.Model.User.AccountModel;
import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;
import com.tranhuutruong.BookStoreAPI.Repository.CartRepository;
import com.tranhuutruong.BookStoreAPI.Repository.ProductDetailRepository;
import com.tranhuutruong.BookStoreAPI.Repository.User.AccountRepository;
import com.tranhuutruong.BookStoreAPI.Repository.User.UserInfoRepository;
import com.tranhuutruong.BookStoreAPI.Request.CartRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService implements CartInterface {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public ApiResponse<Object> addToCart(String username, CartRequest cartRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        if(accountModel == null || accountModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Account is not exists").status(0).build();
        }
        Optional<UserInfoModel> userInfoModel =  Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel(accountModel));
        if (!userInfoModel.isPresent()) {
            return ApiResponse.builder().message("User is not exists").status(0).build();
        }
        Optional<ProductDetailModel> productDetailModel = productDetailRepository.findById(cartRequest.getIdProduct());
        if(!productDetailModel.isPresent())
        {
            return ApiResponse.builder().message("Product is not exists").status(0).build();
        }
        if(cartRequest.getAmount() < 1 || productDetailModel.get().getCurrent_number() < cartRequest.getAmount())
        {
            return ApiResponse.builder().message("Invalid product quantity or insufficient product quantity").status(0).build();
        }
        CartModel cartModel = cartRepository.findCartModelByUserInfoModel_AccountModel_UsernameAndProductDetail_Id(username, cartRequest.getIdProduct());
        if(cartModel == null || cartModel.getId() < 1)
        {
            CartModel newProduct = CartModel.builder()
                    .userInfoModel(userInfoModel.get())
                    .productDetailModel(productDetailModel.get())
                    .amount(cartRequest.getAmount()).build();
            cartRepository.save(newProduct);
            return ApiResponse.builder().status(200).message("Add to cart successful").data(newProduct).build();
        }
        else {
            cartModel.setAmount(cartModel.getAmount() + cartRequest.getAmount());
            cartRepository.save(cartModel);
            return ApiResponse.builder().message("Your cart is updated").status(200).build();
        }
    }

    @Override
    public ApiResponse<Object> updateCart(String username, CartRequest cartRequest)
    {
        CartModel cartModel = cartRepository.findCartModelByUserInfoModel_AccountModel_UsernameAndProductDetail_Id(username, cartRequest.getIdProduct());
        if(cartModel == null || cartModel.getId() <= 0)
        {
            return ApiResponse.builder().status(101).message("Update cart false").build();
        }
        if(cartRequest.getAmount() == 0)
        {
            cartRepository.delete(cartModel);
        }
        else{
            cartModel.setAmount(cartRequest.getAmount());
            cartRepository.save(cartModel);
        }
        return ApiResponse.builder().status(200).message("Update cart success!").data(cartModel).build();
    }

    @Override
    public ApiResponse<Object> deleteItem(String username, Long productId)
    {
        CartModel cartModel = cartRepository.findCartModelByUserInfoModel_AccountModel_UsernameAndProductDetail_Id(username, productId);
        if(cartModel == null || cartModel.getId() <= 0)
        {
            return ApiResponse.builder().data(101).message("cannot find item in your cart").build();
        }
        cartRepository.delete(cartModel);
        return ApiResponse.builder().status(200).message("Update cart success!").build();
    }


    @Override
    public ApiResponse<Object> getAllByUser(String username)
    {
        Iterable<CartModel> list = cartRepository.findAllCartMoDelByUserName(username);
        return ApiResponse.builder().message("My Cart").status(200).data(list).build();
    }

}
