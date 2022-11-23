/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kltn.lambdabuy.service.impl;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.kltn.SpringAPILambdaBuy.common.request.authen.LoginDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.authen.RegisterDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.AuthResponse;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.example.kltn.SpringAPILambdaBuy.entities.UserRole;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.lambdabuy.service.AuthenticationService;
import com.kltn.lambdabuy.service.UserService;

/**
 *
 * @author Asus
 */

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	private RestTemplate restTemplate;
	private String crmRestUrl;
	private Logger logger = Logger.getLogger(getClass().getName());
	private ObjectMapper mapper;
	
	@Autowired
	private UserService userService;
	
	public AuthenticationServiceImpl(RestTemplate theRestTemplate, @Value("${crm.rest.url}") String theUrl) {
		restTemplate = theRestTemplate;
		crmRestUrl = theUrl;
		logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
		mapper = new ObjectMapper();
	}

	@Autowired
	private CookieService cookieService;
	
	@Override
	public void seedAdmin() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from
																		// nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

	@Override
	public ResponseCommon register(RegisterDto registerDto) {
		String uri = crmRestUrl + "/authentication/register";
		ResponseEntity<ResponseCommon> response = restTemplate.postForEntity(uri, registerDto, ResponseCommon.class);
		return response.getBody();
	}

	@Override
	
    public AuthResponse login(LoginDto loginDto) {
    	String uri = crmRestUrl + "/authentication/login";
    	ResponseEntity<ResponseCommon> response = restTemplate.postForEntity(uri, loginDto, ResponseCommon.class);
    	if(response.getBody().success) {
    		AuthResponse auth = mapper.convertValue(response.getBody().data, new TypeReference<AuthResponse>() {});
    		
    		UserResponseDto user = userService.findByEmail(auth.getEmail());
    		if(user.getRole() == UserRole.CUSTOMER) {
    			return auth;
    		}
    	}
    	return null;
//         UserEntity user = restTemplate.getForObject(crmRestUrl+"/authentication/login", UserEntity.class);
//         if(user!=null){
//             if(user.isEnabled()==true){
//                 if(bCryptPasswordEncoder.matches(loginDto.getPassword(), user.getPassword())){
//                     return user;
//                 } else return null;
//             } else return null;
//         } else return null;
       
    }

}
