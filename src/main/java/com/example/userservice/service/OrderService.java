package com.example.userservice.service;

import com.example.userservice.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    @Value("${orderservice.url}")
    private String orderServiceUrl;

    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        String url = orderServiceUrl + userId;  
        Order[] orders = restTemplate.getForObject(url, Order[].class);
        return Arrays.asList(orders);
    }
}
