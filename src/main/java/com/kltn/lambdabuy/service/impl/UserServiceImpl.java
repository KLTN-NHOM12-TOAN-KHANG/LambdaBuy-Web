/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kltn.lambdabuy.service.impl;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.kltn.SpringAPILambdaBuy.entities.UserEntity;
import com.kltn.lambdabuy.service.UserService;

/**
 *
 * @author Asus
 */
@Service
public class UserServiceImpl implements UserService {
    private RestTemplate restTemplate;
    private String crmRestUrl;
    private Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    public UserServiceImpl(RestTemplate theRestTemplate, 
	        @Value("${crm.rest.url}") String theUrl) {
		restTemplate = theRestTemplate;
	        crmRestUrl = theUrl;
	        logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
    }
    
    @Override
    @Transactional
    public List<UserEntity> findAll() {
        ResponseEntity<List<UserEntity>> responseEntity =
                restTemplate.exchange(crmRestUrl+"/users",HttpMethod.GET,null,
                       new ParameterizedTypeReference<List<UserEntity>>() {});
        List<UserEntity> users = responseEntity.getBody();
        return users;
    }

    @Override
    @Transactional
    public UserEntity findById(String id) {
        UserEntity result=restTemplate.getForObject(crmRestUrl+"/users"+id, UserEntity.class);
        return result;
    }

    @Override
    public void create(UserEntity entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
