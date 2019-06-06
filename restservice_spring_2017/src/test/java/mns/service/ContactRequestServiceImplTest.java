package mns.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.mns.dao.ContactRequestDAO;
import com.mns.dto.ContactRequest;
import com.mns.dto.ContactRequestRes;
import com.mns.dto.EmailUser;
import com.mns.dto.User;
import com.mns.service.MailService;
import com.mns.service.impl.ContactRequestServiceImpl;

import mns.util.TestUtil;

public class ContactRequestServiceImplTest {

	private ContactRequestServiceImpl contactRequestService;

	private ContactRequestDAO contactRequestDAO;

	private MailService mailService;

	@Before
	public void setUp() {

		contactRequestService = new ContactRequestServiceImpl();
		contactRequestDAO = Mockito.mock(ContactRequestDAO.class);
		mailService = Mockito.mock(MailService.class);
		contactRequestService.setContactRequestDAO(contactRequestDAO);
		//contactRequestService.setMailService(mailService);

	}

	/*
	@Test
	public void processContactRequest() throws Exception {

		ContactRequest contactRequest = TestUtil.prepareContactRequest();
		User user = TestUtil.prepareUserWithMaker();
		Map<String, User> usersMap = new HashMap<String, User>();
		usersMap.put("1", user);
		Mockito.when(contactRequestDAO.getUsersWithMakers(contactRequest.getToUserIds())).thenReturn(usersMap);
		Mockito.when(contactRequestDAO.createContactRequest(contactRequest)).thenReturn(contactRequest);
		Mockito.doNothing().when(contactRequestDAO).createContactRequestXMaker(contactRequest, usersMap);

		//emailing removed from ContactRequestService
		EmailUser emailUser = Mockito.mock(EmailUser.class);
		Mockito.when(mailService.sendMail2(emailUser)).thenReturn("success");
		ContactRequestRes contactRequestRes = contactRequestService.processContactRequest(contactRequest);
		assertNotNull(contactRequestRes);
	}

	@Test
	public void processContactRequestException() throws Exception {
		ContactRequest contactRequest = TestUtil.prepareContactRequest();
		User user = TestUtil.prepareUserWithMaker();
		Map<String, User> usersMap = new HashMap<String, User>();
		usersMap.put("1", user);
		Mockito.when(contactRequestDAO.getUsersWithMakers(contactRequest.getToUserIds())).thenReturn(usersMap);
		Mockito.when(contactRequestDAO.createContactRequest(contactRequest)).thenReturn(contactRequest);
		Mockito.doNothing().when(contactRequestDAO).createContactRequestXMaker(contactRequest, usersMap);

		//emailing removed from ContactRequestService
		EmailUser emailUser = Mockito.mock(EmailUser.class);
		Mockito.when(mailService.sendMail2(emailUser)).thenThrow(Exception.class);
		ContactRequestRes contactRequestRes = contactRequestService.processContactRequest(contactRequest);
		assertTrue(contactRequestRes.getErrorInfos().isEmpty());
	}
	*/
}
