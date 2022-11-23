/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kltn.lambdabuy.service;

import java.util.List;

import com.example.kltn.SpringAPILambdaBuy.common.request.authen.TokenDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.user.CreateUserProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.user.UpdateUserProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.example.kltn.SpringAPILambdaBuy.entities.UserEntity;

/**
 *
 * @author Asus
 */
public interface UserService {
	public UserResponseDto currentUser();
    public List<UserResponseDto> findAll();
	public UserResponseDto findById(String id);
	public UserResponseDto findByEmail(String email);
	public UserResponseDto findByUsername(String username);
	public void create(UserEntity entity);
	public ResponseCommon<?> updateUserProfile(UpdateUserProfileDto updateUserProfileDto);
	
	void delete(String id);
}
