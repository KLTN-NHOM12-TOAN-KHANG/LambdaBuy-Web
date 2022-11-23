package com.kltn.lambdabuy.service.impl;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.kltn.SpringAPILambdaBuy.common.request.profile.UpdateProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.lambdabuy.service.ProductService;
import com.kltn.lambdabuy.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {
	private RestTemplate restTemplate;
	private ObjectMapper mapper;
	private String crmRestUrl;
    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private CookieService cookieService;
    
    public ProfileServiceImpl(RestTemplateBuilder restTemplateBuilder, 
	        @Value("${crm.rest.url}") String theUrl) {
		restTemplate = restTemplateBuilder.basicAuthentication("username", "password").build();
		mapper = new ObjectMapper();
		crmRestUrl = theUrl;
        logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
	}
    
    @Override
	public ProfileResponseDto getProfileById(String id) {
    	String uri = crmRestUrl + "/profile/" + id;
    	
    	try {
    		String accessToken = null;
    		Cookie cookieAccessToken = cookieService.readAccessToken("access_token");
        	if(cookieAccessToken != null) {
        		accessToken = cookieAccessToken.getValue();
        	}
    		String token = "Bearer " + accessToken;
    		ResponseEntity<ResponseCommon> response = restTemplate.getForEntity(uri, ResponseCommon.class);
    		if(response.getBody().success) {
    			ProfileResponseDto profile = mapper.convertValue(response.getBody().data, new TypeReference<ProfileResponseDto>() {});
    			return profile;
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	@Override
	public ResponseCommon updateProfile(UpdateProfileDto updateProfileDto) {
		String uri = crmRestUrl + "/profile/update";
		
		try {
			String accessToken = null;
    		Cookie cookieAccessToken = cookieService.readAccessToken("access_token");
        	if(cookieAccessToken != null) {
        		accessToken = cookieAccessToken.getValue();
        	}
    		String token = "Bearer " + accessToken;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", token);
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("id", updateProfileDto.getId());
			map.add("phoneNumber", updateProfileDto.getPhoneNumber());
			map.add("address", updateProfileDto.getAddress());
			map.add("avatar", updateProfileDto.getAvatar());
			map.add("firstName", updateProfileDto.getFirstName());
			map.add("lastName", updateProfileDto.getLastName());	
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

			ResponseEntity<ResponseCommon> response = restTemplate.exchange(uri, HttpMethod.POST, request, ResponseCommon.class, map);

			if(response.getBody().success) {
				 return response.getBody();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
