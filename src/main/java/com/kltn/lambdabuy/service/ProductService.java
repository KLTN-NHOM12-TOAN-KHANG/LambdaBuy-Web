package com.kltn.lambdabuy.service;

import java.util.List;

import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.example.kltn.SpringAPILambdaBuy.entities.ProductEntity;

public interface ProductService {
	public List<ProductResponseDto> findAll();
	public ProductResponseDto findById(String id);
	public ProductEntity findProductEntityById(String id);
}
