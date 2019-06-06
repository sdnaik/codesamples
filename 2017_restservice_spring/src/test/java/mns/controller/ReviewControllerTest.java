package mns.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.mns.controller.ReviewController;
import com.mns.dto.ReviewRequest;
import com.mns.exceptions.InvalidDataException;
import com.mns.exceptions.ResourceNotFoundException;
import com.mns.service.ReviewService;
import com.mns.validators.ReviewValidator;

@RunWith(MockitoJUnitRunner.class)
public class ReviewControllerTest {

	private ReviewController reviewController;

	private ReviewValidator reviewValidator;
 
	private ReviewService reviewService;

	private ReviewRequest mockReviewReq;

	@Before
	public void setUp() {
		reviewController = new ReviewController();
		reviewValidator = Mockito.mock(ReviewValidator.class);
		reviewService = Mockito.mock(ReviewService.class);
		reviewController.setReviewValidator(reviewValidator);
		reviewController.setReviewService(reviewService);

		mockReviewReq = new ReviewRequest();
		mockReviewReq.setIdMaker(1);
	}

	@Test
	public void createReview() {

		BindingResult result = Mockito.mock(BindingResult.class);
		BDDMockito.given(result.hasErrors()).willReturn(false);
		BDDMockito.given(reviewService.createReview(mockReviewReq)).willReturn(mockReviewReq);
		ReviewRequest reviewRequestRes = reviewController.createReview(mockReviewReq, result);
		assertNotNull(reviewRequestRes);
	}

	@Test(expected = InvalidDataException.class)
	public void createReviewError() {

		BindingResult result = Mockito.mock(BindingResult.class);
		BDDMockito.given(result.hasErrors()).willReturn(true);
		List<ObjectError> errors = new ArrayList<ObjectError>();
		BDDMockito.given(result.getAllErrors()).willReturn(errors);
		reviewController.createReview(mockReviewReq, result);
	}

	@Test
	public void readReview() {
		long reviewId = 1L;
		BDDMockito.given(reviewService.readReview(reviewId)).willReturn(mockReviewReq);
		ReviewRequest reviewRequestRes = reviewController.readReview(reviewId);
		assertNotNull(reviewRequestRes);
	}

	@Test(expected = InvalidDataException.class)
	public void readReviewInvalidReviewId() {
		long reviewId = 0;
		reviewController.readReview(reviewId);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void readReviewInvalidResponse() {
		long reviewId = 1L;
		reviewController.readReview(reviewId);
	}

	/*
	@Test
	public void updateReview() {
		mockReviewReq.setIdReview(1);
		BindingResult result = Mockito.mock(BindingResult.class);
		BDDMockito.given(reviewService.updateReview(mockReviewReq.getIdReview(), mockReviewReq))
				.willReturn(mockReviewReq);
		ReviewRequest reviewRequestRes = reviewController.updateReview(mockReviewReq.getIdReview(), mockReviewReq,
				result);
		assertNotNull(reviewRequestRes);
	}
	
	
	@Test(expected = InvalidDataException.class)
	public void updateReviewnvalidReviewId() {
		mockReviewReq.setIdReview(0);
		BindingResult result = Mockito.mock(BindingResult.class);
		reviewController.updateReview(mockReviewReq.getIdReview(), mockReviewReq, result);
	}

	
	@Test
	public void deleteReview() {
		long reviewId = 1L;
		BDDMockito.given(reviewService.deleteReview(reviewId)).willReturn(true);
		Map<String, Object> map = reviewController.deleteReview(reviewId);
		assertEquals(true, map.get("deleted"));
	}
	*/
}
