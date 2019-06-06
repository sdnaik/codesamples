package com.mns.service;

import java.util.List;
import com.mns.dto.ReviewRequest;

public interface ReviewService {

	public ReviewRequest createReview(ReviewRequest reviewRequest);

	public List<ReviewRequest> getAllReviews(long idMaker);
		
	public ReviewRequest readReview(long reviewId);

	public ReviewRequest updateReview(long reviewId, ReviewRequest reviewRequest);

	public boolean deleteReview(long reviewId);

}

