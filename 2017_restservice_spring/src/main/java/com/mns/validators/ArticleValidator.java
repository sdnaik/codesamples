package com.mns.validators;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.mns.dto.Article;

@Component
public class ArticleValidator implements Validator {

	private static Logger logger = LoggerFactory.getLogger(ArticleValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return Article.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("Validation of Article request starts");
		Article article = (Article) target;
		if (article.getIdMaker() == 0) {
			errors.rejectValue("idMaker", "Invalid Maker Id");
		}

		String keywords = article.getKeywords();
		if (keywords == null) {
			errors.rejectValue("keywords", "Invalid Keywords");
		}

		String title = article.getTitle();
		if (title == null) {
			errors.rejectValue("title", "Invalid Title");
		}
		List<MultipartFile> files = article.getAttachments();
		String link = article.getLink();

		if (files.isEmpty() && link == null || "".equals(link)) {
			errors.reject("attachments/links should not be empty");
		}

		if (errors.hasErrors()) {
			logger.debug("Validation errors exists, please fix beiow errors to proceed further.\n");
		} else {
			logger.debug("Successfully validated all the fields.");
		}

	}

}
