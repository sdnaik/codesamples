package com.mns.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mns.constants.DbQueries;
import com.mns.dto.ReviewRequest;
import com.mns.exceptions.ServerException;

@Repository
public class ReviewDAO {

	private static final Logger logger = LoggerFactory.getLogger(ReviewDAO.class);

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public ReviewRequest createReview(final ReviewRequest reviewRequest) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		logger.debug("Create review sql: " + DbQueries.CREATE_REVIEW);

		try {
			final int isVerifiedEmail = verifyReviewerEmail(reviewRequest);
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(DbQueries.CREATE_REVIEW, new String[] { "id" });
					ps.setLong(1, reviewRequest.getIdMaker());
					ps.setInt(2, reviewRequest.getRating());
					ps.setString(3, reviewRequest.getTitle());
					ps.setString(4, reviewRequest.getDescription());
					ps.setString(5, reviewRequest.getReviewerName());
					ps.setString(6, reviewRequest.getReviewerEmail());
					ps.setString(7, reviewRequest.getReviewerZip());
					ps.setInt(8, isVerifiedEmail);
					ps.setInt(9, reviewRequest.getHelpfulCount());
					return ps;
				}
			}, keyHolder);
		} catch (DataAccessException e) {
			logger.error("Error while creating the user. " + e);
			throw new ServerException("Create review exception", e);
		}
		return readReviewRequest((Long) keyHolder.getKey());
	}
	
	
	public List<ReviewRequest> getAllReviews(final long idMaker) {
		logger.debug("read all reviews by id sql: " + DbQueries.READ_ALL_REVIEWS_BYMAKERID);
		logger.debug("read all reviews only  parameter : " + idMaker);
		List<ReviewRequest> reviews = new ArrayList<ReviewRequest>();	
		try {
			List<Map<String, Object>> dbResults = jdbcTemplate.queryForList(DbQueries.READ_ALL_REVIEWS_BYMAKERID, new Object[] { idMaker });
			
			for (Map<String, Object> row : dbResults) {
				if(row != null) {
					ReviewRequest review = new ReviewRequest();					
					review.setIdReview((Integer)row.get("id_review"));
					review.setIdMaker((Integer)row.get("idMaker"));
					if(row.get("rating") != null) {
						review.setRating((Integer)row.get("rating"));	
					}
					if(row.get("title") != null) {
						review.setTitle((String)row.get("title"));	
					}
					if(row.get("description") != null) {
						review.setDescription((String)row.get("description"));	
					}
					if(row.get("reviewer_name") != null) {
						review.setReviewerName((String)row.get("reviewer_name"));	
					}
					if(row.get("reviewer_email") !=null ){
						review.setReviewerEmail((String)row.get("reviewer_email"));	
					}
					if(row.get("reviewer_zip") != null) {
						review.setReviewerZip((String)row.get("reviewer_zip"));	
					}
					if(row.get("verified") !=null) {
						review.setVerified((Integer)row.get("verified"));	
					}
					if(row.get("helpful_count") != null) {
						review.setHelpfulCount((Integer)row.get("helpful_count"));	
					}					
					reviews.add(review);					
				}
			}
							
		} catch (EmptyResultDataAccessException ex) {
			throw new ServerException("No records found for the maker id: " + idMaker);
		} catch (DataAccessException e) {
			logger.error("error while accessing the review data.", e);
			throw new ServerException("Read review exception", e);
		}
		return reviews;
	}
	

	public ReviewRequest readReviewRequest(final long reviewId) {
		logger.debug("read review by id sql: " + DbQueries.READ_ONLY_REVIEW_BYID);
		logger.debug("read review only  parameter : " + reviewId);
		ReviewRequest reviewRequest;
		try {
			reviewRequest = this.jdbcTemplate.queryForObject(DbQueries.READ_ONLY_REVIEW_BYID, new Object[] { reviewId },
					new RowMapper<ReviewRequest>() {
						public ReviewRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
							ReviewRequest reviewReq = new ReviewRequest();
							reviewReq.setIdReview(rs.getLong("id_review"));
							reviewReq.setIdMaker(rs.getLong("idmaker"));
							reviewReq.setRating(rs.getInt("rating"));
							reviewReq.setTitle(rs.getString("title"));
							reviewReq.setDescription(rs.getString("description"));
							reviewReq.setReviewerName(rs.getString("reviewer_name"));
							reviewReq.setReviewerEmail(rs.getString("reviewer_email"));
							reviewReq.setReviewerZip(rs.getString("reviewer_zip"));
							reviewReq.setVerified(rs.getInt("verified"));
							reviewReq.setHelpfulCount(rs.getInt("helpful_count"));
							return reviewReq;
						}
					});
		} catch (EmptyResultDataAccessException ex) {
			throw new ServerException("No records found for the review id: " + reviewId);
		} catch (DataAccessException e) {
			logger.error("error while accessing the review data.", e);
			throw new ServerException("Read review exception", e);
		}
		return reviewRequest;
	}

	public ReviewRequest updateReview(long reviewId, ReviewRequest reviewRequest) {

		StringBuilder sql = new StringBuilder("update review set ");

		List<String> paramsFileds = new ArrayList<String>();
		List<Object> paramsList = new ArrayList<Object>();
		try {

			long idMaker = reviewRequest.getIdMaker();
			if (idMaker != 0) {
				paramsFileds.add("idmaker = ?");
				paramsList.add(idMaker);
			}

			int rating = reviewRequest.getRating();

			if (rating != 0) {
				paramsFileds.add("rating = ?");
				paramsList.add(rating);
			}
			
			String title = reviewRequest.getTitle();
			if (title != null) {
				paramsFileds.add("title = ?");
				paramsList.add(title);
			}			
			
			String descritpion = reviewRequest.getDescription();
			if (descritpion != null) {
				paramsFileds.add("description = ?");
				paramsList.add(descritpion);
			}

			String reviewerName = reviewRequest.getReviewerName();
			if (reviewerName != null) {

				paramsFileds.add("reviewer_name = ?");
				paramsList.add(reviewerName);
			}

			String reviewerEmail = reviewRequest.getReviewerEmail();
			if (reviewerEmail != null) {
				paramsFileds.add("reviewer_email = ?");
				paramsList.add(reviewerEmail);
			}

			String reviewerZip = reviewRequest.getReviewerZip();
			if (reviewerZip != null) {
				paramsFileds.add("reviewer_zip = ?");
				paramsList.add(reviewerZip);
			}

			int verified = reviewRequest.getVerified();
			if (verified != 0) {
				paramsFileds.add("verified = ?");
				paramsList.add(verified);
			}

			int helpfulCount = reviewRequest.getHelpfulCount();
			if (helpfulCount != 0) {
				paramsFileds.add("helpful_count = ?");
				paramsList.add(helpfulCount);
			}

			sql.append(StringUtils.join(paramsFileds, ","));

			sql.append(" where id_review = ?");

			paramsList.add(reviewId);

			logger.debug("update sql = " + sql.toString());

			logger.debug("Update review parameters");

			int paramsListCounter = 0;
			for (Object param : paramsList) {
				paramsListCounter++;
				logger.debug(paramsListCounter +"->" + param.toString());
			}

			int updated = this.jdbcTemplate.update(sql.toString(), paramsList.toArray());
			logger.info("Updated review status : " + updated);
		} catch (DataAccessException e) {
			logger.error("Error while updating review. " + e);
			throw new ServerException("Update review exception ", e);
		}

		return readReviewRequest(reviewId);
	}

	public boolean deleteReview(final long reviewId) {
		logger.debug("deleting the review in dao with id {}", reviewId);
		int update = 0;
		boolean status = false;
		try {
			update = this.jdbcTemplate.update("delete from review where id_review = ?", Long.valueOf(reviewId));
			logger.debug("review delete status : " + update);
			if (update != 0) {
				status = true;
			}
		} catch (DataAccessException e) {
			logger.error("Error while deleting the review. " + e);
			throw new ServerException("Review delete exception", e);
		}
		return status;
	}
	
	
	
	/*
	 * Add a method to verifyReviewer. 
	 * This method will call verifyReviewerByCookie, verifyReviewerByEmail (rename the one below) and others in future to verify reviewer
	 */
	
	public int verifyReviewerEmail(ReviewRequest reviewRequest) {
		logger.debug("verify email in contact request table sql: " + DbQueries.VERIFIFY_EMAIL_IN_CONTACTREQUEST);
		logger.debug("verify review parameters with email {}, with idmaker {}", reviewRequest.getReviewerEmail(),
				reviewRequest.getIdMaker());
		int isVerified = 0;
		try {

			List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(DbQueries.VERIFIFY_EMAIL_IN_CONTACTREQUEST,
					new Object[] { reviewRequest.getReviewerEmail(), reviewRequest.getIdMaker() });

			if (!rows.isEmpty()) {
				isVerified = 1;
			}

			logger.debug("verified reviewer email status : " + isVerified);
		} catch (EmptyResultDataAccessException ex) {
			logger.error("No records found for the reviewe in contact request table: ");
		} catch (DataAccessException e) {
			logger.error("error while accessing the verifying reviewer email.", e);
		}

		return isVerified;
	}
}
