package com.example.demo.services;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Link;
import com.example.demo.entities.Transaction;
import com.example.demo.entities.User;
import com.example.demo.entities.UserRole;
import com.example.demo.repositories.Hierarchy;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserRoleRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class UserServices {
	
	@Autowired private UserRepository userRepo;
	@Autowired private UserRoleRepository userRoleRepo;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private Hierarchy hierachyRepo;
	@Autowired private TransactionRepository transRepo;
	@Autowired private JavaMailSender mailSender;
	
	public void register(User user, String siteURL) throws UnsupportedEncodingException, MessagingException
	{
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);
		user.setPasswordChangedTime(new Date());
		
		userRepo.save(user);
		
        UserRole userRole = new UserRole(user.getId(), 2);
        userRoleRepo.save(userRole);
		
		sendVerificationEmail(user, siteURL);
	}
	
	private void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException
	{
		String toAddress = user.getEmail();
	    String fromAddress = "mysmtpservernevexis@gmail.com";
	    String senderName = "Nevexis Ltd.";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>"
	            + "Nevexis Ltd.";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", user.getFirstName());
	    String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}
	
	public boolean verify(String verificationCode)
	{
		User user = userRepo.findByVerificationCode(verificationCode);
		
		if(user == null || user.isEnabled())
		{
			return false;
		}
		else
		{
			user.setVerificationCode(null);
			user.setEnabled(true);
			userRepo.save(user);
			return true;
		}
	}
	
	public List<User> getUsers() {
		return userRepo.findAll();
	}
	
	public User getUserByUsername(String username)
	{
		User user = userRepo.findByUsername(username);
		return user;
	}
	
	public void saveNewEmployee(User parent, User child) {
		String encodedPassword = passwordEncoder.encode(child.getPassword());
		child.setPassword(encodedPassword);
		child.setEnabled(true);
		userRepo.save(child);
		userRoleRepo.save(new UserRole(child.getId(), 1));
		hierachyRepo.save(new Link(child.getId(), parent.getId()));
	}

	public void saveNewTransaction(User client, User employee, Transaction transaction) {
		switch (transaction.getPrice()) {
		case 40:
			transaction.setName("product_1");
			break;
		case 60:
			transaction.setName("product_2");
			break;
		case 80:
			transaction.setName("product_3");
			break;
		}
		transaction.setClientId(client.getId());
		transaction.setSellerId(employee.getId());
		transaction.setCreationDateTime(new Date().getTime());
		transRepo.save(transaction);
	}
	
	public void changePassword(User user, String newPassword)
	{
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);
		user.setPasswordChangedTime(new Date());
		userRepo.save(user);
	}
}
