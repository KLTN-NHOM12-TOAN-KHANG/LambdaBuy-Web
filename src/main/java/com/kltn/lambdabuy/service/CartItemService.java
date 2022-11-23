package com.kltn.lambdabuy.service;

import java.util.Collection;

import com.example.kltn.SpringAPILambdaBuy.common.request.cart.CartDto;

public interface CartItemService {

	double getAmmount();

	int getCount();

	Collection<CartDto> getAllItems();

	void clear();

	CartDto update(String productId, int quantity);

	void remove(String id);

	void add(CartDto item);
	
	CartDto findByName(String name);

}
