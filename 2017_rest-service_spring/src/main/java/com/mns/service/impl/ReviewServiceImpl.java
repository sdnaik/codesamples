package com.mns.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mns.dao.ReviewDAO;
import com.mns.dto.EmailUser;
import com.mns.dto.ReviewRequest;
import com.mns.exceptions.ResourceNotFoundException;
import com.mns.exceptions.ServerException;
import com.mns.service.MailService;
import com.mns.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

	@Autowired 
	private ReviewDAO reviewDAO;

	@Autowired
	private MailService mailService;

	private static final boolean EMAIL_FLAG = false;

	@Override
	public ReviewRequest createReview(ReviewRequest reviewRequest) {
		logger.debug("Creating review in service layer");
		try {
			Long reviewId = reviewRequest.getIdReview();
			if (reviewId != 0) {
				throw new ServerException("Review exists already with review id " + reviewId);
			}
			reviewRequest = reviewDAO.createReview(reviewRequest);

			if (EMAIL_FLAG) {
				EmailUser emailUser = prepareEmailRequest(reviewRequest);

				mailService.sendMail2(emailUser);
			}

		} catch (ServerException e) {
			throw e;
		}
		logger.debug("Created review in service layer");
		return reviewRequest;
	}

	public EmailUser prepareEmailRequest(ReviewRequest reviewRequest) {
		EmailUser emailUser = new EmailUser();
		List<String> tos = new ArrayList<String>();
		tos.add(reviewRequest.getReviewerEmail());
		emailUser.setTos(tos);
		emailUser.setSubject("New Review Created");
		emailUser.setText("Thanks for creating the review");
		return emailUser;
	}

	@Override
	public List<ReviewRequest> getAllReviews (long idMaker) {
		logger.debug("Reading all reviews in service layer for maker id {}", idMaker);		
		List<ReviewRequest> reviews = new ArrayList<ReviewRequest>();
		try {
			reviews = reviewDAO.getAllReviews(idMaker);
		} catch (ServerException e) {
			throw e;
		}
		logger.debug("Read all reviews in service layer for maker id {} ", idMaker);
		return reviews;
	}
	
	
	@Override
	public ReviewRequest readReview(long reviewId) {
		logger.debug("Reading review in service layer with id {}", reviewId);
		ReviewRequest reviewRequest = null;
		try {
			reviewRequest = reviewDAO.readReviewRequest(reviewId);
		} catch (ServerException e) {
			throw e;
		}
		logger.debug("Read the review in service layer with id {} ", reviewId);
		return reviewRequest;
	}

	@Override
	@Transactional
	public ReviewRequest updateReview(long reviewId, ReviewRequest reviewRequest) {

		logger.debug("Updating review in service layer with id {} ", reviewId);
		try {
			ReviewRequest reviewRequestDB = reviewDAO.readReviewRequest(reviewId);
			if (reviewRequestDB == null) {
				throw new ResourceNotFoundException();
			}

			reviewRequest = reviewDAO.updateReview(reviewId, reviewRequest);

		} catch (ServerException e) {
			throw e;
		}
		logger.debug("Updated the review in service layer with Id {}", reviewId);

		return reviewRequest;
	}

	@Override
	public boolean deleteReview(long reviewId) {
		logger.debug("Deleting the review in service layer with id {}", reviewId);
		boolean deleted = false;
		try {
			deleted = reviewDAO.deleteReview(reviewId);
		} catch (ServerException e) {
			throw e;
		}
		logger.debug("Deleted the review in service layer with id {} ", reviewId);
		return deleted;
	}

	public void setReviewDAO(ReviewDAO reviewDAO) {
		this.reviewDAO = reviewDAO;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

}
