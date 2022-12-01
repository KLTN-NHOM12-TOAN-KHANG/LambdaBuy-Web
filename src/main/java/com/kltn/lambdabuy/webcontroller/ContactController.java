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
import com.kltn.lambdabuy.service.ProfileService;
import com.kltn.lambdabuy.service.UserService;

@Controller
@RequestMapping("/contact")
public class ContactController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@GetMapping("/view")
	public String viewContact(Model theModel) {
		UserResponseDto user = userService.currentUser();
    	theModel.addAttribute("user", user);
    	if(user != null) {
    		ProfileResponseDto profile = user.getProfile();
    		theModel.addAttribute("profile", profile);
    	}
    	int inCart = cartItemService.getCount();
    	theModel.addAttribute("inCart", inCart);
		return "views/contact/view";
	}
}
