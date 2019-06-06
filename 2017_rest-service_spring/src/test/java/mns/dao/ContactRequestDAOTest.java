package mns.dao;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.mns.dao.ContactRequestDAO;
import com.mns.dao.UserDAO;
import com.mns.dto.ContactRequest;

import mns.util.TestUtil;

public class ContactRequestDAOTest {

	private ContactRequestDAO contactRequestDAO;

	private UserDAO userDao;

	private DataSource dataSource;

	@Before
	public void setUp() {

		contactRequestDAO = new ContactRequestDAO();
		userDao = Mockito.mock(UserDAO.class);
		dataSource = Mockito.mock(DataSource.class);
		contactRequestDAO.setDataSource(dataSource);
		contactRequestDAO.setUserDao(userDao);

	}

	@Test
	@Ignore
	public void getUsersWithMakers() {
		ContactRequest contactRequest = TestUtil.prepareContactRequest();
		// Whitebox.setInternalState(contactRequestDAO, "jdbcTemplate",
		// jdbcTemplate);
		// Connection connection = Mockito.mock(Connection.class);
		// Whitebox.setInternalState(contactRequestDAO, "connection",
		// connection);
		ContactRequest contactRequestReturn = contactRequestDAO.createContactRequest(contactRequest);
		assertNotNull(contactRequestReturn);
	}

}
