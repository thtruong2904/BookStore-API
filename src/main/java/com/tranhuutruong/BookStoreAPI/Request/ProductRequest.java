package com.tranhuutruong.BookStoreAPI.Request;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

@Data
@Builder
public class ProductRequest {

    private String category;

    private String name;

    private String description;

    private FilePart image;

    private Long price;

    private String author;
}
