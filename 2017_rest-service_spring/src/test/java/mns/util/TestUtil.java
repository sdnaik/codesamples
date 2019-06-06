package mns.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mns.dto.ContactRequest;
import com.mns.dto.Maker;
import com.mns.dto.User;

public class TestUtil {

	public static ContactRequest prepareContactRequest() {
		ContactRequest contactRequest = new ContactRequest();
		contactRequest.setFromEmail("satishnaidu.a@gmail.com");
		contactRequest.setEmailText("Email Text");
		contactRequest.setEmailSubject("Hi Maker");
		contactRequest.setIdContactrequest(1l);
		List<MultipartFile> files = contactRequest.getAttachments();
		MockMultipartFile mockMultipartFile = new MockMultipartFile("test.txt", // filename
				"Hallo World".getBytes()); // content

		files.add(mockMultipartFile);
		contactRequest.setAttachments(files);
		List<String> userIds = new ArrayList<String>();
		userIds.add("1");
		userIds.add("2");
		contactRequest.setToUserIds(userIds);
		return contactRequest;
	}

	public static User prepareUserWithMaker() {
		User user = new User();
		user.setIdUser(1);
		user.setEmail("satishnaidu.a@gmail.com");
		user.setName("Test maker name");
		Maker maker = new Maker();
		maker.setIdMaker(1);
		maker.setIdUser(1);
		user.setMaker(maker);
		return user;
	}
}
