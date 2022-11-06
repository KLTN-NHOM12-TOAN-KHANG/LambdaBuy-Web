package com.kltn.lambdabuy.service.impl;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.kltn.SpringAPILambdaBuy.entities.BrandEntity;
import com.kltn.lambdabuy.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {
	 private RestTemplate restTemplate;
	    private String crmRestUrl;
	    private Logger logger = Logger.getLogger(getClass().getName());
	    @Autowired
	    public BrandServiceImpl(RestTemplate theRestTemplate, 
	        @Value("${crm.rest.url}") String theUrl) {
		restTemplate = theRestTemplate;
	        crmRestUrl = theUrl;
	        logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
		}
		@Override
		public List<BrandEntity> findAll() {
			// TODO Auto-generated method stub
			 ResponseEntity<List<BrandEntity>> responseEntity = 
			            restTemplate.exchange(crmRestUrl+"/brands/", HttpMethod.GET, null, 
			            new ParameterizedTypeReference<List<BrandEntity>>() {});
			            // get the list of customers from response
			            List<BrandEntity> brands = responseEntity.getBody();
			            logger.info("in getBrands(): brands" + brands);
			            return brands;
		}
		@Override
		public BrandEntity findById(String id) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void create(BrandEntity entity) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void delete(String id) {
			// TODO Auto-generated method stub
			
		}
}
