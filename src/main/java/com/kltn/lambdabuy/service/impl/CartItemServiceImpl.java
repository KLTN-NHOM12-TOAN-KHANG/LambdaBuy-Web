package com.kltn.lambdabuy.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import com.example.kltn.SpringAPILambdaBuy.common.request.cart.Cart;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.lambdabuy.service.CartItemService;

@SessionScope
@Service
public class CartItemServiceImpl implements CartItemService {

	private RestTemplate restTemplate;
	private String crmRestUrl;
	private ObjectMapper mapper;
	private Logger logger = Logger.getLogger(getClass().getName());

	private Map<String, Cart> maps = new HashMap<>();
	
	@Autowired
	public CartItemServiceImpl(RestTemplate theRestTemplate, @Value("${crm.rest.url}") String theUrl) {
		restTemplate = theRestTemplate;
		mapper = new ObjectMapper();
		crmRestUrl = theUrl;
		logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
	}

	@Override
	public void add(Cart item) {
		Cart cartItem = maps.get(item.getProductId());
		if(cartItem == null) {
			maps.put(item.getProductId(), item);
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
		}
	}

	@Override
	public void remove(String id) {
		maps.remove(id);
	}
	
	@Override
	public Cart update(String productId, int quantity) {
		Cart cart = maps.get(productId);
		cart.setQuantity(quantity);
		return cart;
	}

	@Override
	public void clear() {
		maps.clear();
	}
	
	@Override
	public Collection<Cart> getAllItems() {
		return maps.values();
	}
	
	@Override
	public int getCount() {
		return maps.values().size();
	}
	
	@Override
	public double getAmmount() {
		return maps.values().stream()
					.mapToDouble(item -> item.getQuantity() * item.getPrice())
					.sum();
	}

}
