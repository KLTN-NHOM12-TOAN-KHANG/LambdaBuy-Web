/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kltn.lambdabuy.webcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.kltn.lambdabuy.service.CartItemService;
import com.kltn.lambdabuy.service.ProductService;
import com.kltn.lambdabuy.service.UserService;
import com.kltn.lambdabuy.service.impl.CookieService;

/**
 *
 * @author Asus
 */
@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CookieService cookieService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
    @GetMapping({"", "/shop"})
    public String test(Model theModel){
    	UserResponseDto user = userService.currentUser();
    	List<ProductResponseDto> products = productService.findAll();
    	theModel.addAttribute("products", products);
    	theModel.addAttribute("user", user);
    	if(user != null) {
    		ProfileResponseDto profile = user.getProfile();
    		theModel.addAttribute("profile", profile);
    	}
    	int inCart = cartItemService.getCount();
    	theModel.addAttribute("inCart", inCart);
        return "views/shop";
    }
}
