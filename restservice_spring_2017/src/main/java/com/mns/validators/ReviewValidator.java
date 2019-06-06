package com.mns.validators;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mns.dto.ReviewRequest;

@Component
public class ReviewValidator implements Validator {

	private static Logger logger = LoggerFactory.getLogger(ReviewValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return ReviewRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.info("Validation of review request starts");
		ReviewRequest reviewRequest = (ReviewRequest) target;
		logger.debug("valiation object : " + reviewRequest);

		if (reviewRequest.getIdMaker() == 0) {
			errors.rejectValue("idMaker", "Invalid Maker Id");
		}

		if (reviewRequest.getRating() == 0) {
			errors.rejectValue("rating", "Invalid Rating");
		}

		if (StringUtils.isEmpty(reviewRequest.getTitle())) {
			errors.rejectValue("title", "Title is required");
		}
		
		if (StringUtils.isEmpty(reviewRequest.getDescription())) {
			errors.rejectValue("description", "Description s required");
		}

		if (StringUtils.isEmpty(reviewRequest.getReviewerName())) {
			errors.rejectValue("reviewerName", "Reviewer name is required");
		}

		String reviewerEmail = reviewRequest.getReviewerEmail();
		if (!StringUtils.isEmpty(reviewerEmail)) {
			boolean isValidEmail = EmailValidator.validateEmail(reviewerEmail);
			if (!isValidEmail) {
				errors.rejectValue("reviewerEmail", "Invalid Email");
			}
		}

		if (errors.hasErrors()) {
			logger.debug("Validation errors exists, please fix beiow errors to proceed further.\n");
		} else {
			logger.debug("Successfully validated all the fields.");
		}

	}

}