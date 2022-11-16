package com.kltn.lambdabuy.service;

import java.util.Collection;

import com.example.kltn.SpringAPILambdaBuy.common.request.cart.Cart;

public interface CartItemService {

	double getAmmount();

	int getCount();

	Collection<Cart> getAllItems();

	void clear();

	Cart update(String productId, int quantity);

	void remove(String id);

	void add(Cart item);

}
