package com.kltn.lambdabuy.webcontroller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.kltn.SpringAPILambdaBuy.common.request.image.UploadImageDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.profile.UpdateProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.user.UpdateUserProfileDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ImageResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ResponseCommon;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.example.kltn.SpringAPILambdaBuy.entities.ImageEntity;
import com.example.kltn.SpringAPILambdaBuy.entities.ProfileEntity;
import com.kltn.lambdabuy.service.CartItemService;
import com.kltn.lambdabuy.service.ProfileService;
import com.kltn.lambdabuy.service.StorageService;
import com.kltn.lambdabuy.service.UserService;
import com.kltn.lambdabuy.service.impl.CookieService;
import com.kltn.lambdabuy.utils.ImageUtil;

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
	
	@Autowired
	private StorageService storageService;
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
    		if(profile != null) {
    			byte[] image = storageService.download(profile.getAvatar());
    			if(image != null) {
    				String base64 = Base64.getEncoder().encodeToString(image);
        			if(base64 != null) {
        				model.addAttribute("base64", base64);
        			}else {
        				model.addAttribute("base64", "aa");
        			}
    			}
    		}
    	}
    	int inCart = cartItemService.getCount();
    	model.addAttribute("inCart", inCart);
		model.addAttribute("user", user);
		
		return "views/user/info";
	}
	
	@PostMapping("/update-info")
	public String updateUser(@RequestParam("avatar") MultipartFile avatar,
							@RequestParam("firstname") String firstName,
							@RequestParam("lastname") String lastName,
							@RequestParam("phoneNumber") String phoneNumber,
							@RequestParam("address") String address,
							HttpServletRequest request) throws IOException {
		UserResponseDto user = userService.currentUser();
		
		UpdateProfileDto updateProfileDto = new UpdateProfileDto(user.getProfile().getId(), phoneNumber, address, null, firstName, lastName);
		
		ImageEntity img = new ImageEntity();
		img.setName(Math.random() + avatar.getOriginalFilename());
		img.setType(avatar.getContentType());
		img.setImageData(ImageUtil.compressImage(avatar.getBytes()));
		String data = Base64.getEncoder().encodeToString(img.getImageData());
		
		UploadImageDto uploadImageDto = new UploadImageDto(img.getName(), img.getType(), data);
		ImageEntity image =  storageService.upload(uploadImageDto);
		
		updateProfileDto.setAvatar(img.getName());
//		ImageEntity findImage = storageService.findById(image.getId());
//		ProfileEntity profile = profileService.getProfileEntityById(user.getProfile().getId());
//		profile.setImage(findImage);
		
//		byte[] avatarByte = storageService.download(img.getName());
//		if(avatarByte != null) {
//			String base64 = Base64.getEncoder().encodeToString(avatarByte);
//			updateProfileDto.setAvatar(data);
//		}
//		String avatarImage = StringUtils.cleanPath(avatar.getOriginalFilename());
//		if(avatarImage.contains(".."))
//		{
//			System.out.println("not a a valid file");
//		}
//		try {
//			updateProfileDto.setAvatar(Base64.getEncoder().encodeToString(avatar.getBytes()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		ResponseCommon<?> response = profileService.updateProfile(updateProfileDto);
		if(!response.success) {
			request.setAttribute("message", "Có lỗi xảy ra!");
		}
		request.setAttribute("message", "Cập nhật thông tin cá nhân thành công");
		return "redirect:/user/info";
	}
}
