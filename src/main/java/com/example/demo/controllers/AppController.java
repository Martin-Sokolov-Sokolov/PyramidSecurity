package com.example.demo.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Transaction;
import com.example.demo.entities.User;
import com.example.demo.services.UserServices;

@Controller
public class AppController {

	@Autowired
	private UserServices userService;

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}

	@PostMapping("/process_register")
	public String processRegister(User user, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		userService.register(user, getSiteURL(request));
		return "register_success";
	}

	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code) {
		if (userService.verify(code)) {
			return "verify_success";
		} else {
			return "verify_fail";
		}
	}

	@GetMapping("/users")
	public Callable<String> showUsers(Model model) {
		Callable<String> callable = () -> {
			List<User> listUsers = userService.getUsers();
			model.addAttribute("listUsers", listUsers);
			return "users";
		};
		return callable;
	}

	@GetMapping("/products")
	public String showProducts(Model model) {
		model.addAttribute("transaction", new Transaction());
		return "products";
	}

	@PostMapping("/process_transaction")
	public String processTransaction(Transaction transaction, Model model, HttpServletRequest request) {
		String clientUsername = currentUserName(request);
		User client = userService.getUserByUsername(clientUsername);
		User employee = userService.getUserByUsername(transaction.getUsernameEmployee());
		userService.saveNewTransaction(client, employee, transaction);
		return "process_transaction_success";
	}

	@GetMapping("/new")
	public String addNewEmpoyee(Model model) {
		model.addAttribute("user", new User());
		return "add_employee";
	}

	@PostMapping("/process_new_employee")
	public String processEmployee(User child, Model model, HttpServletRequest request) {
		User parent = userService.getUserByUsername(currentUserName(request));
		userService.saveNewEmployee(parent, child);
		return "process_new_employee_success";
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@RequestMapping(value = "/username", method = RequestMethod.GET)
	@ResponseBody
	public String currentUserName(HttpServletRequest request) {
		return request.getUserPrincipal().getName();
	}

}
