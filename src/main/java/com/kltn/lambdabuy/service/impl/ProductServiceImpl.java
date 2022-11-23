package com.kltn.lambdabuy.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.kltn.SpringAPILambdaBuy.common.request.authen.LoginDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.AuthResponse;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.example.kltn.SpringAPILambdaBuy.entities.OrderEntity;
import com.example.kltn.SpringAPILambdaBuy.entities.ProductEntity;
import com.example.kltn.SpringAPILambdaBuy.entities.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.lambdabuy.service.AuthenticationService;
import com.kltn.lambdabuy.service.ProductService;
import com.kltn.lambdabuy.service.UserService;

@Service
public class ProductServiceImpl implements ProductService {
	private RestTemplate restTemplate;
	private ObjectMapper mapper;
	private String crmRestUrl;
    private Logger logger = Logger.getLogger(getClass().getName());
    static Cipher cipher;
    @Autowired
	public ProductServiceImpl(RestTemplateBuilder restTemplateBuilder, 
	        @Value("${crm.rest.url}") String theUrl) {
		restTemplate = restTemplateBuilder.basicAuthentication("username", "password").build();
		mapper = new ObjectMapper();
		crmRestUrl = theUrl;
        logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
	}
    
    @Autowired
    private CookieService cookieService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationService authenticationService;
	
    @Override
    @Transactional
	public List<ProductResponseDto> findAll() {
//    	String uri = crmRestUrl + "/products";
//    	Cookie cookieAT = cookieService.readAccessToken("access_token");
//    	if(cookieAT != null) {
//    		String accessToken = cookieAT.getValue();
//    		System.out.println(accessToken);
//    	}
//    	String result = null;
//    	ResponseEntity<ResponseCommon> response = restTemplate.getForEntity(uri, ResponseCommon.class);
//        if(response.getBody().success) {
//        	List<ProductResponseDto> listProduct = mapper.convertValue(response.getBody().data, new TypeReference<List<ProductResponseDto>>() {});
//        	List<ProductResponseDto> result1 = new ArrayList<>();
//        	for (ProductResponseDto productResponseDto : listProduct) {
//				if(!productResponseDto.getIsDeleted()) {
//					result1.add(productResponseDto);
//				}
//			}
//        	return result1;
//        }
//    	return null;
    	
    	String uri = crmRestUrl + "/products";
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
				ResponseEntity<ResponseCommon> returnResponse = restTemplate.exchange(uri, HttpMethod.GET, jwtEntity,
						ResponseCommon.class);
				if (returnResponse.getBody().success) {
					List<ProductResponseDto> listProduct = mapper.convertValue(returnResponse.getBody().data, new TypeReference<List<ProductResponseDto>>() {});
		        	List<ProductResponseDto> result1 = new ArrayList<>();
		        	for (ProductResponseDto productResponseDto : listProduct) {
						if(!productResponseDto.getIsDeleted()) {
							result1.add(productResponseDto);
						}
					}
		        	return result1;
				}
			
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return null;
	}
    
    @Override
    @Transactional
    public ProductResponseDto findById(String id) {
    	String uri = crmRestUrl + "/product/" + id;
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
				ProductResponseDto product = mapper.convertValue(response.getBody().data, new TypeReference<ProductResponseDto>() {});
				return product;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public ProductEntity findProductEntityById(String id) {
		String uri = crmRestUrl + "/product/entity/" + id;
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
				ProductEntity product = mapper.convertValue(response.getBody().data, new TypeReference<ProductEntity>() {});
				return product;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

//    private RegistrationUser getRegistrationUser() {
//		RegistrationUser user = new RegistrationUser();
//		user.setUsername("javainuse");
//		user.setPassword("javainuse");
//		user.setRole("ROLE_ADMIN");
//		return user;
//	}
//
//	private UserResponseDto getAuthenticationUser() {
//		UserResponseDto user = userService.currentUser();
//		if(user != null) {
//			return user;
//		}
//		return null;
//	}
//
//	private HttpHeaders getHeaders() {
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//		return headers;
//	}
//
//	private String getBody(final UserResponseDto user) throws JsonProcessingException {
//		return new ObjectMapper().writeValueAsString(user);
//	}
//	
//	public static String decrypt(String encryptedText, SecretKey secretKey)
//            throws Exception {
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] encryptedTextByte = decoder.decode(encryptedText);
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
//        String decryptedText = new String(decryptedByte);
//        return decryptedText;
//    }
}
