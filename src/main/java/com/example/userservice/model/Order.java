package com.example.userservice.model;

import java.util.List;
import lombok.Data;

@Data
public class Order {
    private Long orderId;
    private Long userId;
    private String orderStatus;
    private Double totalAmount;
    private List<OrderDetail> orderDetails;
}
