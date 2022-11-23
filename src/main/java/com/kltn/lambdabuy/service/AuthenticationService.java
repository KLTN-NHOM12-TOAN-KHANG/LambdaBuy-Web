/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kltn.lambdabuy.service;

import com.example.kltn.SpringAPILambdaBuy.common.request.authen.LoginDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.authen.RegisterDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.AuthResponse;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.example.kltn.SpringAPILambdaBuy.entities.UserEntity;

/**
 *
 * @author Asus
 */
public interface AuthenticationService {
    void seedAdmin();
	ResponseCommon register(RegisterDto registerDto); 
	AuthResponse login(LoginDto loginDto);
}
