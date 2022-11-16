/*
0 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kltn.lambdabuy.webcontroller;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kltn.SpringAPILambdaBuy.common.request.authen.LoginDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.authen.RegisterDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.cart.Cart;
import com.example.kltn.SpringAPILambdaBuy.common.response.AuthResponse;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.kltn.lambdabuy.service.AuthenticationService;
import com.kltn.lambdabuy.service.impl.CookieService;

@Controller
@RequestMapping("/")
public class AuthenticationController {
	@Autowired
	private HttpSession session;
	
	@Autowired
	private CookieService cookie;
	
	@Autowired
	private ServletContext app;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@GetMapping("/register")
	public String register(Model model) {
//		Cookie cookieFn = cookie.read("fn");
//		Cookie cookieLn = cookie.read("ln");
//		Cookie cookieEmail = cookie.read("email");
//		Cookie cookieUsername = cookie.read("username");
//		Cookie cookiePassword = cookie.read("password");
//		Cookie cookieRepassword = cookie.read("repassword");
//		
//		String firstName = cookieFn.getValue();
//		String lastName = cookieLn.getValue();
//		String email = cookieEmail.getValue();
//		String username = cookieUsername.getValue();
//		String password = cookiePassword.getValue();
//		String rePassword = cookieRepassword.getValue();
//		
//		model.addAttribute("fn", firstName);
//		model.addAttribute("ln", lastName);
//		model.addAttribute("email", email);
//		model.addAttribute("username", username);
//		model.addAttribute("password", password);
//		model.addAttribute("repassword", password);
//		
		return "views/authentication/register";
	}
	
	@PostMapping("/register")
	public String register(Model model,
				@RequestParam("fn") String firstName,
				@RequestParam("ln") String lastName,
				@RequestParam("email") String email,
				@RequestParam("username") String username,
				@RequestParam("password") String password,
				@RequestParam("repassword") String rePassword) {
		
		RegisterDto registerDto = new RegisterDto();
		registerDto.setFirstName(firstName);
		registerDto.setLastName(lastName);
		registerDto.setEmail(email);
		registerDto.setUsername(username);
		registerDto.setPassword(password);
		registerDto.setRePassword(rePassword);
		
		ResponseCommon<?> response = authenticationService.register(registerDto);
		if(!response.success) {
			model.addAttribute("message", response.message);
			return "views/authentication/register";
		}
		model.addAttribute("message", "Đăng ký thành công. Vui lòng kiểm tra mail để kích hoạt tài khoản!");
		return "views/authentication/login";
	}
	
    @GetMapping("/login")
    public String login(Model model){
//    	Cookie cookieId = cookie.read("userid");
//    	Cookie cookiePass = cookie.read("pass");
//    	if(cookieId != null) {
//    		String uid = cookieId.getValue();
//    		String pwd = cookiePass.getValue();
//    		
//    		model.addAttribute("uid", uid);
//    		model.addAttribute("pwd", pwd);
//    	}
        return "views/authentication/login";
    }
    
    @PostMapping("/login")
    public String login(Model model, 
    			@RequestParam("id") String id,
    			@RequestParam("pw") String pw,
    			@RequestParam(value = "rm", defaultValue = "false") boolean rm) {
    	LoginDto loginDto = new LoginDto();
    	loginDto.setUsername(id);
    	loginDto.setEmail(id);
    	loginDto.setPassword(pw);
    	AuthResponse user = authenticationService.login(loginDto);
    	cookie.createAccessToken("access_token", user.getAccessToken(), 10);
//    	HashMap<String, Cart> carts = new HashMap<>();
//    	cookie.create("myCartItems", carts.values()., 10);
    	if(user == null) {
    		model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu!");
    	}  else {
    		model.addAttribute("message", "Đăng nhập thành công!");
    		session.setAttribute("user", user);
//    		if(rm == true) {
//    			cookie.create("pass", user.getPassword(), 30);
//    		} else {
//    			cookie.delete("userid");
//    			cookie.delete("pass");
//    		}
    		
    		String backUrl = (String) session.getAttribute("back-url");
    		if(backUrl != null) {
    			return "redirect:" + backUrl;
    		}
    		return "redirect:/index";
    	}
    	return "views/authentication/login";
    }
    
    @GetMapping("/logout")
    public String logout(Model model) {
    	session.removeAttribute("user");
    	cookie.delete("access_token");
    	return "redirect:/";
    }
    
}
