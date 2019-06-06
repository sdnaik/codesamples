package com.mns.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mns.dto.LoginRequest;
import com.mns.dto.User;
import com.mns.dto.ValidationRequest;
import com.mns.exceptions.InvalidDataException;
import com.mns.exceptions.ResourceNotFoundException;
import com.mns.exceptions.AuthorizationException;
import com.mns.service.SecurityService;

@RestController
@RequestMapping(value = "/security", produces = "application/vnd.captech-v1.0+json", consumes = "application/vnd.captech-v1.0+json")
public class SecurityController {
	
	static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
	
	@Autowired
	private SecurityService securityService;
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}


	@RequestMapping(value = "/", method = RequestMethod.POST)
	public User authenticate(@RequestBody LoginRequest loginRequest) {
		logger.debug("Start processing the login request with input {}", loginRequest);
		User user = securityService.authenticate(loginRequest);
		if (user == null){
			throw new AuthorizationException();
		}
		logger.debug("Successfully completed the login request");
		return user;
	}
	

	//Do not provide access to detokenize method to public
	//Detokenize should happen only internally
	@RequestMapping(value = "/{userId}", method = RequestMethod.POST)
	public @ResponseBody String tokenize(@PathVariable long userId, @RequestBody LoginRequest loginRequest) {
		logger.debug("Security controller called to tokenize the user id; userId {} ", userId);
		if (userId <= 0 || loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() || loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
			throw new InvalidDataException();
		}
		User user = securityService.authenticate(loginRequest);
		if (user == null) {
			throw new AuthorizationException();
		}
		
		String token = null;
		if (user !=null) {
			token = securityService.tokenize(userId); 
		}
		if (token == null) {
			throw new ResourceNotFoundException();
		}
		return token;
	}
	
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public @ResponseBody boolean doesUserExist(@RequestBody String email) {
		logger.debug("Security controller called to verify email {} ", email);
		if (email == null || (email!= null && email.trim() == "" )) {
			throw new InvalidDataException();
		}
		return securityService.doesUserExist(email);
	}

	/*
	 * returns SUCCESS - if email was found and password was sent
	 * returns NOTFOUND - if email wasn't found 
	 * returns ERROR_EMAIL - if error occurRed while sending email
	 * returns ERROR_FINDPASSWORD - if something went wrong in the process
	 */
	@RequestMapping(value = "/forgotpw", method = RequestMethod.POST)
	public @ResponseBody String emailPassword(@RequestBody String email) {
		logger.debug("Security controller called to send password for the email {} ", email);
		if (email == null || (email!= null && email.trim() == "" )) {
			throw new InvalidDataException();
		}
		return securityService.emailPassword(email);
	}

	
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public @ResponseBody boolean validateToken(@RequestBody ValidationRequest validationRequest) {
		String token = validationRequest.getToken();
		long userId = validationRequest.getUserId();
		logger.debug("Security controller called to validate token {} ", token);
		if (token == null || (token!= null && token.trim() == "" || userId <= 0)) {
			throw new InvalidDataException();
		}
		return securityService.validateToken(token, userId);
	}
	

	 
	
}
