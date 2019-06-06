package com.mns.service;

import com.mns.dto.User;
import com.mns.dto.LoginRequest;

public interface SecurityService {
	

	public User authenticate(LoginRequest loginRequest);
	
	//The token will be provided only during the login or registration when a valid user id and password are provided
	public String tokenize(long userId);
	
	public String detokenize(String token);	
	
	public boolean doesUserExist(String email);
	
	public String emailPassword(String email);
	
	public boolean validateToken(String token, long userId);
	
	

}
