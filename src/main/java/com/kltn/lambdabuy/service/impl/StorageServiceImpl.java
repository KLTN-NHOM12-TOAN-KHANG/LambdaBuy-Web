package com.kltn.lambdabuy.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.kltn.SpringAPILambdaBuy.common.request.image.UploadImageDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ImageResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.entities.ImageEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kltn.lambdabuy.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {
	private RestTemplate restTemplate;
	private ObjectMapper mapper;
	private String crmRestUrl;
    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private CookieService cookieService;
    
    public StorageServiceImpl(RestTemplateBuilder restTemplateBuilder, 
	        @Value("${crm.rest.url}") String theUrl) {
		restTemplate = restTemplateBuilder.basicAuthentication("username", "password").build();
		mapper = new ObjectMapper();
		crmRestUrl = theUrl;
        logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
	}
    
	@Override
	public ImageEntity upload(UploadImageDto uploadImageDto) {
		String uri = crmRestUrl + "/api/image/upload";
		
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
    		map.add("name", uploadImageDto.getName());
    		map.add("type", uploadImageDto.getType());
    		map.add("imageData", uploadImageDto.getImageData());
    		
    		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

			HttpEntity<ResponseCommon> response = restTemplate.exchange(
			        uri,
			        HttpMethod.POST,
			        request,
			        ResponseCommon.class,
			        uploadImageDto
			);
			if(response.getBody().success) {
				ImageEntity image = mapper.convertValue(response.getBody().data, new TypeReference<ImageEntity>() {});
				return image;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public byte[] download(String avatarName) {
		String uri = crmRestUrl + "/api/image/name/" + avatarName;
		
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
				byte[] imageData = mapper.convertValue(response.getBody().data, new TypeReference<byte[]>() {});
				return imageData;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public ImageEntity findById(String id) {
		String uri = crmRestUrl + "/api/image/" + id;
		
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
				ImageEntity image = mapper.convertValue(response.getBody().data, new TypeReference<ImageEntity>() {});
				return image;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
