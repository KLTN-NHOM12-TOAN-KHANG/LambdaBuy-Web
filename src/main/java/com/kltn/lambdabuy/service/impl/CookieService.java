 package com.kltn.lambdabuy.service.impl;

import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	public Cookie create(String name, String value, int days) {
		String encodedValue = Base64.getEncoder().encodeToString(value.getBytes());
		Cookie cookie = new Cookie(name, encodedValue);
		cookie.setMaxAge(days * 24 * 60 * 60);
		cookie.setPath("/");
		response.addCookie(cookie);
		return cookie;
	}
	
	public Cookie createAccessToken(String name, String value, int days) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(days * 24 * 60 * 10);
		cookie.setPath("/");
		response.addCookie(cookie);
		return cookie;
	}

	public Cookie read(String name) {
		Cookie[] cookies = request.getCookies(); //đọc từ client
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase(name)) {
					String decodedValue = new String(Base64.getDecoder().decode(cookie.getValue()));
					cookie.setValue(decodedValue);
					return cookie;
				}
			}
		}
		return null;
	}
	
	public Cookie readAccessToken(String name) {
		Cookie[] cookies = request.getCookies(); //đọc từ client
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase(name)) {
					cookie.setValue(cookie.getValue());
					return cookie;
				}
			}
		}
		return null;
	}

	public void delete(String name) {
		this.create(name, "", 0); //thời hạn tồn tại cookie = 0
	}
}
