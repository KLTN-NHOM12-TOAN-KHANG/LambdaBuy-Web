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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.kltn.SpringAPILambdaBuy.common.request.order.CreateOrderDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.OrderResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.entities.OrderEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.lambdabuy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	private RestTemplate restTemplate;
	private ObjectMapper mapper;
	private String crmRestUrl;
    private Logger logger = Logger.getLogger(getClass().getName());
    static Cipher cipher;
    
	public OrderServiceImpl(RestTemplateBuilder restTemplateBuilder, 
	        @Value("${crm.rest.url}") String theUrl) {
		restTemplate = restTemplateBuilder.basicAuthentication("username", "password").build();
		mapper = new ObjectMapper();
		crmRestUrl = theUrl;
        logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
	}
	@Autowired
	private CookieService cookieService;
	
	
	@Override
	public OrderResponseDto findById(String id) {
		String uri = crmRestUrl + "/order/" + id;
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
				OrderResponseDto order = mapper.convertValue(response.getBody().data, new TypeReference<OrderResponseDto>() {});
				return order;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public OrderEntity findOrderEntityById(String id) {
		String uri = crmRestUrl + "/order/entity/" + id;
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
				OrderEntity order = mapper.convertValue(response.getBody().data, new TypeReference<OrderEntity>() {});
				return order;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	@Override
	public OrderResponseDto create(CreateOrderDto createOrderDto) {
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
		
		String uri = crmRestUrl + "/order/create";
		
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(uri)
		        .queryParam("subTotal", "{subTotal}")
		        .queryParam("shipping", "{shipping}")
		        .queryParam("tax", "{tax}")
		        .queryParam("total", "{total}")
//		        .queryParam("listProductId", "{listProductId}")
		        .encode()
		        .toUriString();
		
		Map<String, String> params = new HashMap<>();
		params.put("subTotal", String.valueOf(createOrderDto.getSubTotal()));
		params.put("shipping", String.valueOf(createOrderDto.getShipping()));
		params.put("tax", String.valueOf(createOrderDto.getTax()));
		params.put("total", String.valueOf(createOrderDto.getTotal()));
		
//		String listProductId = "";
//		for (String productId : createOrderDto.getListProductId()) {
//			listProductId += productId + ", ";
//		}
//		params.put("listProductId", listProductId);
		
		HttpEntity<ResponseCommon> response = restTemplate.exchange(
		        urlTemplate,
		        HttpMethod.GET,
		        entity,
		        ResponseCommon.class,
		        params
		);
		if(response.getBody().success) {
			OrderResponseDto order = mapper.convertValue(response.getBody().data, new TypeReference<OrderResponseDto>() {});
			return order;
		}
		return null;
	}
}
