package mns.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mns.controller.ContactRequestController;
import com.mns.dto.ContactRequest;
import com.mns.dto.ContactRequestRes;
import com.mns.service.ContactRequestService;



public class SpringInegrationTest {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "dispatcher-servlet.xml", "service-context.xml" });

		System.out.println(context);

		ContactRequestController contactRequestController = (ContactRequestController) context
				.getBean("contactRequestController");

		ContactRequestService contactRequestService = (ContactRequestService) context
				.getBean("contactRequestService");
		System.out.println(contactRequestController);
		ContactRequest contactRequest = TestUtil.prepareContactRequest();

		ContactRequestRes contactRequestRes = contactRequestService.processContactRequest(contactRequest);

		System.out.println(contactRequestRes);

	}
}
