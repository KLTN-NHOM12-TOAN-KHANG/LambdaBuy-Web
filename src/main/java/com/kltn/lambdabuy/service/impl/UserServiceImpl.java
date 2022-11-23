/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kltn.lambdabuy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.kltn.SpringAPILambdaBuy.common.request.user.UpdateUserProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.example.kltn.SpringAPILambdaBuy.entities.UserEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.lambdabuy.service.UserService;

/**
 *
 * @author Asus
 */
@Service
public class UserServiceImpl implements UserService {
    private RestTemplate restTemplate;
    private String crmRestUrl;
    private ObjectMapper mapper;
    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Autowired
    private CookieService cookieService;
    
    @Autowired
    public UserServiceImpl(RestTemplate theRestTemplate, 
	        @Value("${crm.rest.url}") String theUrl) {
			restTemplate = theRestTemplate;
	        crmRestUrl = theUrl;
	        mapper = new ObjectMapper();
	        logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
    }
    
    @Override
    @Transactional
    public UserResponseDto currentUser() {
		String uri = crmRestUrl + "/getCurrentUser";
		String accessToken = null;
		Cookie cookieAccessToken = cookieService.readAccessToken("access_token");
    	if(cookieAccessToken != null) {
    		accessToken = cookieAccessToken.getValue();
    	}
    	
    	if(accessToken != null) {
    		ResponseEntity<ResponseCommon> response = restTemplate.postForEntity(uri, accessToken, ResponseCommon.class);
    		
//    		ResponseEntity<ResponseCommon> response = restTemplate.getForEntity(uri, ResponseCommon.class);
    		if(response.getBody().success) {
    			UserResponseDto user = mapper.convertValue(response.getBody().data, new TypeReference<UserResponseDto>() {});
    			return user;
    		}
    		return null;
    	}
		return null;
		
//		String accessToken = null;
//		TokenDto tokenDto = new TokenDto();
//		Cookie cookieAccessToken = cookieService.readAccessToken("access_token");
//    	if(cookieAccessToken != null) {
//    		accessToken = cookieAccessToken.getValue();
//    		tokenDto.setToken(accessToken);
//    	}
//		
//		if(accessToken != null) {
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//			HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
//			// Use Token to get Response
//			ResponseEntity<ResponseCommon> response = restTemplate.exchange(uri, HttpMethod.POST, jwtEntity,
//					ResponseCommon.class, tokenDto);
//			
//			if(response.getBody().success) {
//				UserResponseDto user = mapper.convertValue(response.getBody().data, new TypeReference<UserResponseDto>() {});
//				System.out.println(user.getEmail());
//				return user;
//			}
//			return null;
//		}
//		return null;
	}
    
    @Override
    @Transactional
    public List<UserResponseDto> findAll() {
    	String uri = crmRestUrl + "/users";
    	
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
			List<UserResponseDto> listUser = mapper.convertValue(response.getBody().data, new TypeReference<List<UserResponseDto>>() {});
			List<UserResponseDto> result = new ArrayList<>();
			for (UserResponseDto userResponseDto : listUser) {
				if(!userResponseDto.getIsLocked()) {
					result.add(userResponseDto);
				}
			}
			return result;
		}
		return null;
    }

    @Override
    @Transactional
    public UserResponseDto findById(String id) {
    	String uri = crmRestUrl + "/user/" + id;
    	
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
			UserResponseDto user = mapper.convertValue(response.getBody().data, new TypeReference<UserResponseDto>() {});
			return user;
		}
		return null;
    }
    
    @Override
    @Transactional
    public UserResponseDto findByEmail(String email) {
    	String uri = crmRestUrl + "/user/email/" + email;
		ResponseEntity<ResponseCommon> response = restTemplate.getForEntity(uri, ResponseCommon.class);
		if(response.getBody().success) {
			UserResponseDto user = mapper.convertValue(response.getBody().data, new TypeReference<UserResponseDto>() {});
			return user;
		}
		return null;
    }
    
    @Override
    @Transactional
    public UserResponseDto findByUsername(String username) {
String uri = crmRestUrl + "/user/name/" + username;
    	
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
			UserResponseDto user = mapper.convertValue(response.getBody().data, new TypeReference<UserResponseDto>() {});
			return user;
		}
		return null;
    }

    @Override
    public void create(UserEntity entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

	@Override
	public ResponseCommon<?> updateUserProfile(UpdateUserProfileDto updateUserProfileDto) {
		// TODO Auto-generated method stub
		return null;
	}

	
    
}
