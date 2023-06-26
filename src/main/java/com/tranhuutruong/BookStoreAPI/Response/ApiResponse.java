package com.tranhuutruong.BookStoreAPI.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
//import com.tranhuutruong.BookStoreAPI.Http.CodeStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
//public class ApiResponse<T> {
//    @Builder.Default
//    private int status = 200;
//    @Builder.Default
//    private int error = 0;
//    @Builder.Default
//    private String message = null;
//    private T data;
//
//    public ApiResponse(int status) {
//        this.status = status;
//    }
//
//    public ApiResponse(int status, int error) {
//        this.status = status;
//        this.error = error;
//    }
//
//    public ApiResponse(T data) {
//        this.data = data;
//        this.status = 200;
//    }
//
//    public  static <T> ApiResponse<T> of(T data){
//        ApiResponse apiResponse = new ApiResponse<T>();
//        apiResponse.setData(data);
//        return apiResponse;
//    }
//
////    public static ApiResponse fromErrorCode(CodeStatus codeStatus){
////        ApiResponse apiResponse = new ApiResponse();
////        apiResponse.setError(codeStatus.getCode());
////        apiResponse.setStatus(codeStatus.getStatus().value());
////        return apiResponse;
////    }
//}

public class ApiResponse<T> {
    private int status;
    private int error;
    private String message;
    private T data;

    private static ApiResponse<?> instance;

    public static synchronized ApiResponse<?> getInstance() {
        if (instance == null) {
            instance = new ApiResponse<>();
        }
        return instance;
    }

    public static <T> ApiResponse<T> of(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setData(data);
        return apiResponse;
    }

    // Các phương thức getter và setter khác

    // ...
}
