package com.kltn.lambdabuy.global;

import java.util.ArrayList;
import java.util.List;

import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;

public class GlobalCart {
	public static List<ProductResponseDto> cart;
	static {
		cart = new ArrayList<ProductResponseDto>();
	}
}
