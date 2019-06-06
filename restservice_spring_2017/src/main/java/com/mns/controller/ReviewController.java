package com.mns.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mns.dto.ReviewRequest;
import com.mns.exceptions.InvalidDataException;
import com.mns.exceptions.ResourceNotFoundException;
import com.mns.service.ReviewService;
import com.mns.validators.ReviewValidator;

@RestController
@RequestMapping(value = "/review", produces = "application/vnd.captech-v1.0+json", consumes = "application/vnd.captech-v1.0+json")
public class ReviewController {

	private static Logger logger = LoggerFactory.getLogger(ReviewController.class);

	@Autowired 
	private ReviewService reviewService;

	@Autowired
	private ReviewValidator reviewValidator;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ReviewRequest createReview(@RequestBody @Valid ReviewRequest reviewRequest, BindingResult result) {

		logger.debug("Started processing create review request :: START");
		reviewValidator.validate(reviewRequest, result);
		if (result.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				sb.append(error.getCode());
				sb.append("\n");
			}
			throw new InvalidDataException(sb.toString());
		}

		ReviewRequest response = reviewService.createReview(reviewRequest);

		logger.debug("Successfully completed create review request :: ENDRQS");
		return response;
	}
	
	@RequestMapping(value = "/all/{idMaker}", method = RequestMethod.GET)
	public List<ReviewRequest> getAllReviews(@PathVariable long idMaker) {
		logger.debug("Started pulling all reviews for the maker id {} ", idMaker);
		if (idMaker <= 0) {
			throw new InvalidDataException();
		}
		
		List<ReviewRequest> response = reviewService.getAllReviews(idMaker);
		if (response == null) {
			throw new ResourceNotFoundException();
		}
		logger.debug("Successfully completed get all reviews request :: ENDRQS");
		return response;
	}
		

	@RequestMapping(value = "/{reviewId}", method = RequestMethod.GET)
	public ReviewRequest readReview(@PathVariable long reviewId) {
		logger.debug("Started processing reading the review with reviewId {} ", reviewId);
		if (reviewId <= 0) {
			throw new InvalidDataException();
		}
		ReviewRequest response = reviewService.readReview(reviewId);
		if (response == null) {
			throw new ResourceNotFoundException();
		}
		logger.debug("Successfully completed the read review request :: ENDRQS");
		return response;
	}

	//Updating and deleting the review is intentionally not exposed to web yet.
	
	/*
	@RequestMapping(value = "/{reviewId}", method = RequestMethod.PUT)
	public ReviewRequest updateReview(@PathVariable long reviewId, @RequestBody @Valid ReviewRequest reviewRequest,
			BindingResult result) {

		logger.debug("Started processing updating the review with reviewId {} ", reviewId);
		if (reviewId <= 0 || reviewRequest.getIdReview() <= 0) {
			throw new InvalidDataException();
		}
		ReviewRequest response = reviewService.updateReview(reviewId, reviewRequest);
		logger.debug("Successfully completed the updating review request :: ENDRQS");
		return response;
	}

	@RequestMapping(value = "/{reviewId}", method = RequestMethod.DELETE)
	public Map<String, Object> deleteReview(@PathVariable long reviewId) {
		logger.debug("Started processing deleting the review with review id {}", reviewId);
		Map<String, Object> response = new HashMap<String, Object>();
		boolean deletedReview = reviewService.deleteReview(reviewId);
		response.put("deleted", deletedReview);
		response.put("id", reviewId);
		logger.debug("Successfully completed the delete review request :: ENDRQS");
		return response;
	}
	*/

	public void setReviewService(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	public void setReviewValidator(ReviewValidator reviewValidator) {
		this.reviewValidator = reviewValidator;
	}

}
