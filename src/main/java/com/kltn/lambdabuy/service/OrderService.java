package com.kltn.lambdabuy.service;

import com.example.kltn.SpringAPILambdaBuy.common.request.order.CreateOrderDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.OrderResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.entities.OrderEntity;

public interface OrderService {
	public OrderResponseDto findById(String id);
	public OrderEntity findOrderEntityById(String id);
	public OrderResponseDto create(CreateOrderDto createOrderDto);
}
