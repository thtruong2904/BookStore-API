package com.tranhuutruong.BookStoreAPI.Service.User;

import com.tranhuutruong.BookStoreAPI.Model.User.*;
import com.tranhuutruong.BookStoreAPI.Repository.User.*;
import com.tranhuutruong.BookStoreAPI.Request.User.UpdateUserRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tranhuutruong.BookStoreAPI.Interface.User.UserInterface;
import com.tranhuutruong.BookStoreAPI.Request.User.LoginRequest;
import com.tranhuutruong.BookStoreAPI.Request.User.UserRegisterRequest;
import com.tranhuutruong.BookStoreAPI.Response.User.LoginResponse;
import com.tranhuutruong.BookStoreAPI.Response.User.UserRegisterResponse;
import com.tranhuutruong.BookStoreAPI.Security.JWT.JwtProvider;
import com.tranhuutruong.BookStoreAPI.Utils.Constants;
import com.tranhuutruong.BookStoreAPI.Utils.Format;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserInterface {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerPointRepository customPointRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public LoginResponse login(LoginRequest loginRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(loginRequest.getUsername());
        if(accountModel == null || accountModel.getId() == null)
        {
            return LoginResponse.builder().message("Tài khoản không hợp lệ!").status(false).build();
        }
        else if(accountModel.isActivity() == false)
        {
            return LoginResponse.builder().message("Tài khoản đã bị khóa!").status(false).build();
        }
        else if(accountModel.getVerificationCode() == null)
        {
            return LoginResponse.builder().message("Vui lòng xác nhận qua mail để sử dụng tài khoản của bạn").status(false).build();
        }
        else if(passwordEncoder.matches(loginRequest.getPassword() + Constants.SALT_DEFAULT, accountModel.getPassword()))
        {
            return buildToken(userInfoRepository.findUserInfoModelByAccountModel(accountModel));
        }
        else {
            return LoginResponse.builder().message("Tài khoản mật khẩu không hợp lệ. Vui lòng thử lại").status(false).build();
        }
    }

    private LoginResponse buildToken(UserInfoModel userInfoModel)
    {
        return LoginResponse.builder().userInfoModel(userInfoModel).accessToken(jwtProvider.createToken(userInfoModel)).status(true).build();
    }


    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) throws MessagingException {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(userRegisterRequest.getUserName());
        if(accountModel != null && accountModel.getId() >= 0)
        {
            return UserRegisterResponse.builder().status(false).message("Tài khoản đã tồn tại. Vui lòng nhập lại!").build();
        }
        if(accountRepository.existsByEmail(userRegisterRequest.getEmail()))
        {
            return UserRegisterResponse.builder().status(false).message("Email đã tồn tại. Vui lòng nhập lại!").build();
        }
        RoleModel roleModel = roleRepository.findByName(userRegisterRequest.getRoleName());
        if(roleModel == null)
        {
            return UserRegisterResponse.builder().status(false).message("Role không tồn tại. Vui lòng kiểm tra lại!").build();
        }
        String saltedPassword = userRegisterRequest.getPassWord() + Constants.SALT_DEFAULT;
        String hashedPassword = passwordEncoder.encode(saltedPassword);

        accountModel = AccountModel.builder().username(userRegisterRequest.getUserName()).isActivity(true).roleModel(roleModel).email(userRegisterRequest.getEmail()).password(hashedPassword).build();
        UserInfoModel userInfoModel =UserInfoModel.builder().firstname(userRegisterRequest.getFirstName())
                .lastname(userRegisterRequest.getLastName())
                .phone(Format.normalPhone(userRegisterRequest.getPhone()))
                .accountModel(accountModel)
                .build();
        accountRepository.save(accountModel);
        userInfoRepository.save(userInfoModel);
        CustomerPointModel customPointModel = new CustomerPointModel(userInfoModel);
        customPointRepository.save(customPointModel);
        ConfirmationTokenModel confirmationTokenModel = new ConfirmationTokenModel(userInfoModel);
        confirmationTokenRepository.save(confirmationTokenModel);
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
        helper.setTo(userInfoModel.getAccountModel().getEmail());
        helper.setSubject("Complete Registration!");
        helper.setFrom("tranhuutruong290401@gmail.com");
        helper.setText("Xin chào! " + userInfoModel.getFirstname() + " " + userInfoModel.getLastname() +
                "\nTo confirm your account, please click here : "
                +"http://localhost:8080/api/auth/confirm-account?token="+confirmationTokenModel.getConfirmationToken(), true);
        emailService.sendEmail(mailMessage);
        return UserRegisterResponse.builder().status(true).message("Tạo tài khoản thành công. Vui lòng kiểm tra mail để xác thực và sử dụng tài khoản!").userInfoModel(userInfoModel).build();
    }

    @Override
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, String confirmationToken)
    {
        ConfirmationTokenModel token = confirmationTokenRepository.findByconfirmationToken(confirmationToken);
        if(token != null)
        {
            AccountModel accountModel = accountRepository.findAccountModelByEmail(token.getUserInfoModel().getAccountModel().getEmail());
            accountModel.setVerificationCode(confirmationToken);
            accountRepository.save(accountModel);
            Optional<UserInfoModel> userInfoModel = Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel(accountModel));
            userInfoRepository.save(userInfoModel.get());
            modelAndView.setViewName("accountVerified");
        }
        else {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @Override
    public ApiResponse<Object> getProfile(String username)
    {
        UserInfoModel userInfoModel = userInfoRepository.findUserInfoModelByUsername(username);
        return ApiResponse.builder().status(200).message("Thông tin người dùng").data(userInfoModel).build();
    }

    @Override
    public ApiResponse<Object> updateProfile(String username, UpdateUserRequest updateUserRequest) {
        UserInfoModel userInfoModel = userInfoRepository.findUserInfoModelByUsername(username);
        if(userInfoModel == null || userInfoModel.getId() <= 0)
        {
            return ApiResponse.builder().status(101).message("Không tìm thấy thông tin người dùng").build();
        }
        if(updateUserRequest.getFirstname() != null && !updateUserRequest.getFirstname().isEmpty())
        {
            userInfoModel.setFirstname(updateUserRequest.getFirstname());
        }
        if(updateUserRequest.getLastname() != null && !updateUserRequest.getLastname().isEmpty()) {
            userInfoModel.setLastname(updateUserRequest.getLastname());
        }
        if(updateUserRequest.getPhone() != null && !updateUserRequest.getPhone().isEmpty())
        {
            userInfoModel.setPhone(updateUserRequest.getPhone());
        }
        if(updateUserRequest.getAddress() != null && !updateUserRequest.getAddress().isEmpty())
        {
            userInfoModel.setAddress(updateUserRequest.getAddress());
        }
        userInfoRepository.save(userInfoModel);
        return ApiResponse.builder().status(200).message("Cập nhật thông tin người dùng thành công!").data(userInfoModel).build();
    }

    @Override
    public ApiResponse<Object> getAllCustomer()
    {
        Iterable<UserInfoModel> listCustomer = userInfoRepository.getAllCustomer();
        return ApiResponse.builder().message("Danh sách khách hàng").status(200).data(listCustomer).build();
    }

    @Override
    public ApiResponse<Object> lockCustomer(Long id)
    {
        Optional<UserInfoModel> userInfoModel = userInfoRepository.findById(id);
        if(!userInfoModel.isPresent())
        {
            return ApiResponse.builder().status(101).message("Không tìm thấy người dùng").build();
        }
        userInfoModel.get().getAccountModel().setActivity(false);
        userInfoRepository.save(userInfoModel.get());
        return ApiResponse.builder().status(200).message("Khóa tài khoản người dùng thành công").build();
    }

    @Override
    public ApiResponse<Object> unlockCustomer(Long id)
    {
        Optional<UserInfoModel> userInfoModel = userInfoRepository.findById(id);
        if(!userInfoModel.isPresent())
        {
            return ApiResponse.builder().status(101).message("Không tìm thấy người dùng").build();
        }
        userInfoModel.get().getAccountModel().setActivity(true);
        userInfoRepository.save(userInfoModel.get());
        return ApiResponse.builder().status(200).message("Mở khóa tài khoản người dùng thành công").build();
    }
}
