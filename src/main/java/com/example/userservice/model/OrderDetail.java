package com.example.userservice.model;

import lombok.Data;

@Data
public class OrderDetail {
    private Long orderDetailId;
    private Long productId;
    private Integer quantity;
    private Double price;
}
