package com.kltn.lambdabuy.webcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.kltn.lambdabuy.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@GetMapping("/detail/{id}")
    public String detail(Model theModel, @PathVariable("id") String id){
		ProductResponseDto product = productService.findById(id);
		theModel.addAttribute("product", product);
        return "views/product/detail";
    }
	
	
}
