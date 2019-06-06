package mns.dao;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mns.dao.ReviewDAO;
import com.mns.dto.ReviewRequest;

@RunWith(MockitoJUnitRunner.class)
public class ReviewDAOTest {

	private ReviewDAO reviewDAO;

	private DataSource dataSource;

	private ReviewRequest reviewRequest;

	@Before
	public void setUp() {

		reviewDAO = new ReviewDAO();
		dataSource = Mockito.mock(DataSource.class);
		reviewDAO.setDataSource(dataSource);

		reviewRequest = new ReviewRequest();
		reviewRequest.setIdMaker(1);
	}

	@Test
	@Ignore
	public void createReview() {
		ReviewRequest reviewRequestRes = reviewDAO.createReview(reviewRequest);
		assertNotNull(reviewRequestRes);
	}
}
