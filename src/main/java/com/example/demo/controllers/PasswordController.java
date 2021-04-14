package com.example.demo.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entities.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.services.UserServices;

@Controller
public class PasswordController {
	@Autowired private UserServices userService;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@GetMapping("/change_password")
	public String showChangePasswordForm(Model model) {
		model.addAttribute("pageTitle", "Change Expired Password");
		return "change_password";
	}
	
	@PostMapping("/change_password")
	public String processChangePassword(HttpServletRequest request, HttpServletResponse response,
	        Model model, RedirectAttributes ra,
	        @AuthenticationPrincipal Authentication authentication) throws ServletException {
	    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	    User user = userService.getUserByUsername(userDetails.getUsername());
	     
	    String oldPassword = request.getParameter("oldPassword");
	    String newPassword = request.getParameter("newPassword");
	     
	    model.addAttribute("pageTitle", "Change Expired Password");
	     
	    if (oldPassword.equals(newPassword)) {
	        model.addAttribute("message", "Your new password must be different than the old one.");
	         
	        return "change_password";
	    }
	     
	    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
	        model.addAttribute("message", "Your old password is incorrect.");          
	        return "change_password";
	         
	    } else {
	        userService.changePassword(user, newPassword);
	        request.logout();
	        ra.addFlashAttribute("message", "You have changed your password successfully. "
	                + "Please login again.");
	         
	        return "redirect:/login";          
	    }
	     
	}
}
