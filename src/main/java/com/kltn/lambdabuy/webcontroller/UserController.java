package com.kltn.lambdabuy.webcontroller;

import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.kltn.SpringAPILambdaBuy.common.request.profile.UpdateProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.user.UpdateUserProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.kltn.lambdabuy.service.CartItemService;
import com.kltn.lambdabuy.service.ProfileService;
import com.kltn.lambdabuy.service.UserService;
import com.kltn.lambdabuy.service.impl.CookieService;

@Controller
@RequestMapping("/user")
public class UserController extends HttpServlet {
	@Autowired
	private UserService userService;
	
	@Autowired 
	private CookieService cookieService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private CartItemService cartItemService;
//	@GetMapping("/getInfo")
//	public String info(Model)
	
	@GetMapping("/info")
	public String userHome(Model model, Principal principal) {
		UserResponseDto user = userService.currentUser();
		if(user == null) {
			return "redirect:/login";
		}
		if(user != null) {
    		ProfileResponseDto profile = user.getProfile();
    		model.addAttribute("profile", profile);
    	}
    	int inCart = cartItemService.getCount();
    	model.addAttribute("inCart", inCart);
		model.addAttribute("user", user);
		
		return "views/user/info";
	}
	
	@PostMapping("/update-info")
	public String updateUser(HttpServletRequest request) {
		UserResponseDto user = userService.currentUser();
		
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		String phoneNumber = request.getParameter("phoneNumber");
		String address = request.getParameter("address");
		String avatar = request.getParameter("avatar");
		
		UpdateProfileDto updateProfileDto = new UpdateProfileDto(user.getProfile().getId(), phoneNumber, address, avatar, firstName, lastName);
		ResponseCommon<?> response = profileService.updateProfile(updateProfileDto);
		if(!response.success) {
			request.setAttribute("message", "Có lỗi xảy ra!");
		}
		request.setAttribute("message", "Cập nhật thông tin cá nhân thành công");
		return "redirect:/user/info";
	}
}
