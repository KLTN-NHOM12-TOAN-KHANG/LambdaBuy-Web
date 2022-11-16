package com.kltn.lambdabuy.webcontroller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kltn.SpringAPILambdaBuy.entities.UserEntity;
import com.kltn.lambdabuy.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/user")
	public String userHome(Model model, Principal principal) {
		if(principal == null) {
			return "redirect:/login";
		}
		String username = principal.getName();
		UserEntity user = userService.findByUsername(username);
		model.addAttribute("user", user);
		
		return "views/user/info";
	}
	
//	@GetMapping("/update-infor")
//	public String updateUser(@ModelAttribute("user") UserEntity user,
//								Model model,
//								RedirectAttributes redirectAttributes,
//								Principal principal) {
//		if(principal == null) {
//			return "redirect:/login";
//		}
////		UserEntity userSaved = 
//	}
}
