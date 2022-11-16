package com.kltn.lambdabuy.service;

import java.util.List;

import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;

public interface ProductService {
	public List<ProductResponseDto> findAll();
	public ProductResponseDto findById(String id);
}
