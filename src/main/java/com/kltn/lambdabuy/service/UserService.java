/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kltn.lambdabuy.service;

import java.util.List;

import com.example.kltn.SpringAPILambdaBuy.entities.UserEntity;

/**
 *
 * @author Asus
 */
public interface UserService {
    public List<UserEntity> findAll();
	
	public UserEntity findById(String id);
	public void create(UserEntity entity);
	
	void delete(String id);
}
