package com.kltn.lambdabuy.service;

import com.example.kltn.SpringAPILambdaBuy.common.response.OrderDetailResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.entities.OrderDetailEntity;

public interface OrderDetailService {
	OrderDetailResponseDto findById(String id);
	ResponseCommon<?> create(OrderDetailEntity orderDetailEntity);
}
