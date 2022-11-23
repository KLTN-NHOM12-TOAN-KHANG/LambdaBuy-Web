package com.kltn.lambdabuy.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.kltn.SpringAPILambdaBuy.common.response.OrderDetailResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.entities.OrderDetailEntity;
import com.example.kltn.SpringAPILambdaBuy.entities.OrderEntity;
import com.example.kltn.SpringAPILambdaBuy.entities.ProductEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.lambdabuy.service.OrderDetailService;
import com.kltn.lambdabuy.service.OrderService;
import com.kltn.lambdabuy.service.ProductService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	
	private RestTemplate restTemplate;
	private ObjectMapper mapper;
	private String crmRestUrl;
    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Autowired
    private CookieService cookieService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
	public OrderDetailServiceImpl(RestTemplateBuilder restTemplateBuilder, 
	        @Value("${crm.rest.url}") String theUrl) {
		restTemplate = restTemplateBuilder.basicAuthentication("username", "password").build();
		mapper = new ObjectMapper();
		crmRestUrl = theUrl;
        logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
	}
    
	@Override
	@Transactional
	public OrderDetailResponseDto findById(String id) {
		String uri = crmRestUrl + "/orderdetail/" + id;
    	try {
    		String accessToken = null;
			Cookie cookieAccessToken = cookieService.readAccessToken("access_token");
	    	if(cookieAccessToken != null) {
	    		accessToken = cookieAccessToken.getValue();
	    	}
			String token = "Bearer " + accessToken;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Authorization", token);
			HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
			// Use Token to get Response
			ResponseEntity<ResponseCommon> response = restTemplate.exchange(uri, HttpMethod.GET, jwtEntity,
					ResponseCommon.class);
			if(response.getBody().success) {
				OrderDetailResponseDto orderDetailDto = mapper.convertValue(response.getBody().data, new TypeReference<OrderDetailResponseDto>() {});
				return orderDetailDto;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public ResponseCommon<?> create(OrderDetailEntity orderDetailEntity) {
		String uri = crmRestUrl + "/orderdetail/create";
		OrderEntity order = orderDetailEntity.getOrder();
		ProductEntity product = orderDetailEntity.getProduct();
		
		try {
			String accessToken = null;
			Cookie cookieAccessToken = cookieService.readAccessToken("access_token");
	    	if(cookieAccessToken != null) {
	    		accessToken = cookieAccessToken.getValue();
	    	}
			String token = "Bearer " + accessToken;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Authorization", token);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			
			String urlTemplate = UriComponentsBuilder.fromHttpUrl(uri)
			        .queryParam("orderId", "{orderId}")
			        .queryParam("productId", "{productId}")
			        .queryParam("description", "{description}")
			        .queryParam("payerFirstName", "{payerFirstName}")
			        .queryParam("payerLastName", "{payerLastName}")
			        .queryParam("payerEmail", "{payerEmail}")
			        .queryParam("recidentName", "{recidentName}")
			        .queryParam("shippingLine1", "{shippingLine1}")
			        .queryParam("shippingCity", "{shippingCity}")
			        .queryParam("shippingCountryCode", "{shippingCountryCode}")
			        .queryParam("shippingPostalCode", "{shippingPostalCode}")
			        .encode()
			        .toUriString();
			
			Map<String, String> params = new HashMap<>();
			params.put("orderId", order.getId());
			params.put("productId", product.getId());
			params.put("description", orderDetailEntity.getDescription());
			params.put("payerFirstName", orderDetailEntity.getPayerFirtName());
			params.put("payerLastName", orderDetailEntity.getPayerLastName());
			params.put("payerEmail", orderDetailEntity.getPayerEmail());
			params.put("recidentName", orderDetailEntity.getRecidentName());
			params.put("shippingLine1", orderDetailEntity.getShippingLine1());
			params.put("shippingCity", orderDetailEntity.getShippingCity());
			params.put("shippingCountryCode", orderDetailEntity.getShippingCountryCode());
			params.put("shippingPostalCode", String.valueOf(orderDetailEntity.getShippingPostalCode()));
			
			HttpEntity<ResponseCommon> response = restTemplate.exchange(
			        urlTemplate,
			        HttpMethod.GET,
			        entity,
			        ResponseCommon.class,
			        params
			);
			return response.getBody();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
