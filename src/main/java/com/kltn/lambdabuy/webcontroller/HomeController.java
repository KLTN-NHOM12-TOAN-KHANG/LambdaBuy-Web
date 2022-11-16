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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.kltn.lambdabuy.global.GlobalCart;
import com.kltn.lambdabuy.service.ProductService;

/**
 *
 * @author Asus
 */
@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private ProductService productService;
	
    @GetMapping("/")
    public String test(Model theModel){
        return "views/home";
    }
    
    @GetMapping("/index")
    public String index(Model theModel){
    	List<ProductResponseDto> products = productService.findAll();
    	theModel.addAttribute("products", products);
    	theModel.addAttribute("cartCount", GlobalCart.cart.size());
        return "views/index";
    }
}
