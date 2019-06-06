package mns.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mns.dao.ReviewDAO;
import com.mns.dto.ReviewRequest;
import com.mns.exceptions.ResourceNotFoundException;
import com.mns.exceptions.ServerException;
import com.mns.service.MailService;
import com.mns.service.impl.ReviewServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

	private ReviewServiceImpl reviewServiceImpl;
 
	private ReviewDAO reviewDAO;

	private MailService mailService;

	private ReviewRequest mockReviewReq;

	@Before
	public void setUp() {
		reviewServiceImpl = new ReviewServiceImpl();
		reviewDAO = Mockito.mock(ReviewDAO.class);
		mailService = Mockito.mock(MailService.class);
		reviewServiceImpl.setReviewDAO(reviewDAO);
		reviewServiceImpl.setMailService(mailService);

		mockReviewReq = new ReviewRequest();
		mockReviewReq.setIdMaker(1);

	}

	@Test
	public void createReview() {
		BDDMockito.given(reviewDAO.createReview(mockReviewReq)).willReturn(mockReviewReq);
		ReviewRequest reviewRequestRes = reviewServiceImpl.createReview(mockReviewReq);
		assertNotNull(reviewRequestRes);
	}

	@Test(expected = ServerException.class)
	public void createReviewIdExists() {
		mockReviewReq.setIdReview(1);
		BDDMockito.given(reviewDAO.createReview(mockReviewReq)).willReturn(mockReviewReq);
		reviewServiceImpl.createReview(mockReviewReq);
	}

	@Test(expected = ServerException.class)
	public void createReviewServerException() {
		BDDMockito.given(reviewDAO.createReview(mockReviewReq)).willThrow(new ServerException("Review exception"));
		reviewServiceImpl.createReview(mockReviewReq);
	}

	@Test
	public void readReview() {
		long reviewId = 1L;
		BDDMockito.given(reviewDAO.readReviewRequest(reviewId)).willReturn(mockReviewReq);
		ReviewRequest reviewRequestRes = reviewServiceImpl.readReview(reviewId);
		assertNotNull(reviewRequestRes);
	}

	@Test(expected = ServerException.class)
	public void readReviewServerException() {
		long reviewId = 1L;
		BDDMockito.given(reviewDAO.readReviewRequest(reviewId)).willThrow(new ServerException("Review exception"));
		reviewServiceImpl.readReview(reviewId);
	}

	@Test
	public void updateReview() {
		long reviewId = 1L;
		BDDMockito.given(reviewDAO.readReviewRequest(reviewId)).willReturn(mockReviewReq);
		BDDMockito.given(reviewDAO.updateReview(reviewId, mockReviewReq)).willReturn(mockReviewReq);
		ReviewRequest reviewRequestRes = reviewServiceImpl.updateReview(reviewId, mockReviewReq);
		assertNotNull(reviewRequestRes);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void updateReviewResourceNotFound() {
		long reviewId = 1L;
		BDDMockito.given(reviewDAO.readReviewRequest(reviewId)).willReturn(null);
		reviewServiceImpl.updateReview(reviewId, mockReviewReq);
	}

	@Test(expected = ServerException.class)
	public void updateReviewServerException() {
		long reviewId = 1L;
		BDDMockito.given(reviewDAO.readReviewRequest(reviewId)).willReturn(mockReviewReq);
		BDDMockito.given(reviewDAO.updateReview(reviewId, mockReviewReq))
				.willThrow(new ServerException("Review exception"));
		reviewServiceImpl.updateReview(reviewId, mockReviewReq);
	}

	@Test
	public void deleteReview() {
		long reviewId = 1L;
		BDDMockito.given(reviewDAO.deleteReview(reviewId)).willReturn(true);
		boolean status = reviewServiceImpl.deleteReview(reviewId);
		assertTrue(status);
	}

	@Test(expected = ServerException.class)
	public void deleteReviewServerException() {
		long reviewId = 1L;
		BDDMockito.given(reviewDAO.deleteReview(reviewId)).willThrow(new ServerException("Review exception"));
		reviewServiceImpl.deleteReview(reviewId);
	}
}
