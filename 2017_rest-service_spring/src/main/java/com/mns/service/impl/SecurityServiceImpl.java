package com.mns.service.impl;

import com.mns.dto.User;
import com.mns.exceptions.ServerException;
import com.mns.service.SecurityService;
import com.mns.util.DESCrypto;
import com.mns.util.DESKeyGenerator;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mns.service.MailService;
import com.mns.constants.MnsConstants;
import com.mns.dao.SecurityDAO;
import com.mns.dto.LoginRequest;
import com.mns.dto.MNSEmail;

@Service
public class SecurityServiceImpl implements SecurityService {

	private static final SecretKey KEY = DESKeyGenerator.getKey();

	static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

	@Autowired
	private SecurityDAO securityDAO;

	@Autowired
	private MailService mailService;

	@Override
	public User authenticate(LoginRequest loginRequest) {
		User user = null;
		try {
			user = securityDAO.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
		} catch (Exception e) {
			logger.error("Error occurred while reading the user ", e);
			throw new ServerException();
		}
		return user;
	}

	@Override
	public boolean validateToken(String token, long userId) {
		boolean result = false;
		if(Long.valueOf(detokenize(token)) == userId){
			result = true;
		}
		return result;
	}


	public String tokenize(long userId){

		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		Date dateobj = new Date();

		String toEncrypt = df.format(dateobj) + userId;
		return DESCrypto.encrypt(KEY, toEncrypt);
	}

	public String detokenize (String token){
		String decryptedString = DESCrypto.decrypt(KEY, token);
		return decryptedString.substring(12);
	}

	public boolean doesUserExist(String email){
		boolean exists = false;
		try {
			exists = securityDAO.doesUserExist(email);
		} catch (Exception e) {
			logger.error("error occured while finding the user by email", e);
			throw new ServerException();
		}
		return exists;
	}

	public String emailPassword(String email){
		String result = "";
		String password = "";
		try {
			password = securityDAO.getUserPassword(email);
			if (password != ""){
				try {
					MNSEmail forgotpwEmail = new MNSEmail();
					forgotpwEmail.setFromAddress(MnsConstants.FROM_EMAIL_FORGOT_PASSWORD);
					forgotpwEmail.setReplyToAddress(MnsConstants.FROM_EMAIL_FORGOT_PASSWORD);
					forgotpwEmail.setSubject("Your access request");
					List<String> emailAddresses = new ArrayList<String>();
					emailAddresses.add(email);
					forgotpwEmail.setToAddresses(emailAddresses);
					forgotpwEmail.setEmailBodyInText("your password is " + password);
					mailService.sendMNSEmail(forgotpwEmail);
					result = "SUCCESS";
				} catch (Exception e) {
					result ="ERROR_EMAIL";
					logger.error("error while sending forgotten password", e);
					throw new ServerException();
				}
			}
			else {
				result = "ERROR_FINDPASSWORD";
			}
		} catch (Exception e) {
			result = "ERROR_FINDPASSWORD";
			logger.error("error occured while finding the user by email", e);
			throw new ServerException();
		}
		return result;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

}
