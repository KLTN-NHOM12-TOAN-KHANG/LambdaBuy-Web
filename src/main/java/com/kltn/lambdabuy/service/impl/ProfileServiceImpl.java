package com.kltn.lambdabuy.service.impl;

import java.util.Base64;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.kltn.SpringAPILambdaBuy.common.request.profile.UpdateProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.entities.ProfileEntity;
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
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Authorization", token);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			
			String urlTemplate = UriComponentsBuilder.fromHttpUrl(uri)
					.queryParam("id", "{id}")
			        .queryParam("firstName", "{firstName}")
			        .queryParam("lastName", "{lastName}")
			        .queryParam("phoneNumber", "{phoneNumber}")
			        .queryParam("address", "{address}")
			        .queryParam("avatar", "{avatar}")
			        .encode()
			        .toUriString();
			
			Map<String, String> params = new HashMap<>();
			params.put("id", updateProfileDto.getId());
			params.put("firstName", updateProfileDto.getFirstName());
			params.put("lastName", updateProfileDto.getLastName());
			params.put("phoneNumber", updateProfileDto.getPhoneNumber());
			params.put("address", updateProfileDto.getAddress());
			params.put("avatar", updateProfileDto.getAvatar());
			
			HttpEntity<ResponseCommon> response = restTemplate.exchange(
			        urlTemplate,
			        HttpMethod.GET,
			        entity,
			        ResponseCommon.class,
			        params
			);
			if(response.getBody().success) {
				 return response.getBody();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public ProfileEntity getProfileEntityById(String id) {
		String uri = crmRestUrl + "/profile/entity/" + id;
    	
    	try {
    		String accessToken = null;
    		Cookie cookieAccessToken = cookieService.readAccessToken("access_token");
        	if(cookieAccessToken != null) {
        		accessToken = cookieAccessToken.getValue();
        	}
    		String token = "Bearer " + accessToken;
    		ResponseEntity<ResponseCommon> response = restTemplate.getForEntity(uri, ResponseCommon.class);
    		if(response.getBody().success) {
    			ProfileEntity profile = mapper.convertValue(response.getBody().data, new TypeReference<ProfileEntity>() {});
    			return profile;
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
