package com.example.demo.components;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.entities.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.services.UserServices;

public class PasswordExpirationFilter implements Filter {
	
	@Autowired
	private UserServices userService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		if(isUrlExcluded(httpRequest)) {
			chain.doFilter(request, response);
			return;
		}
		
		System.out.println("PasswordExpirationFilter");
		
		User user = getLoggedInUser();
		
		if(user != null && user.isPasswordExpired()) {
			showChangePasswordPage(response, httpRequest, user);
		}
		else {
			chain.doFilter(httpRequest, response);
		}
		
	}

	private void showChangePasswordPage(ServletResponse response, HttpServletRequest httpRequest, User user) throws IOException {
		 System.out.println("Customer: " + user.getFirstName() + " - Password Expired:");
		    System.out.println("Last time password changed: " + user.getPasswordChangedTime().toString());
		    System.out.println("Current time: " + new Date());
		     
		    HttpServletResponse httpResponse = (HttpServletResponse) response;
		    String redirectURL = httpRequest.getContextPath() + "/change_password";
		    httpResponse.sendRedirect(redirectURL);
	}

	private User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = null;
		
		if(authentication != null) {
			principal = authentication.getPrincipal();
		}
		if(principal != null && principal instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) principal;
			return userService.getUserByUsername(userDetails.getUsername());
		}
		
		return null;
	}

	private boolean isUrlExcluded(HttpServletRequest httpRequest) {

		String url = httpRequest.getRequestURL().toString();
		
		if(url.endsWith(".css") || url.endsWith(".png") || url.endsWith(".js") || url.endsWith("/change_password"))
			return true;
		
		return false;
	}

	

}
