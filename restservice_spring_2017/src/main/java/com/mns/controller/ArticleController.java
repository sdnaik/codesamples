package com.mns.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mns.constants.MnsConstants;
import com.mns.dto.Article;
import com.mns.exceptions.InvalidDataException;
import com.mns.exceptions.ResourceNotFoundException;
import com.mns.exceptions.AuthorizationException;
import com.mns.service.ArticleService;
import com.mns.service.SecurityService;
import com.mns.service.UserService;
import com.mns.validators.ArticleValidator;

@RestController
@RequestMapping(produces = "application/vnd.captech-v1.0+json")
public class ArticleController {

	private static Logger logger = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ArticleValidator articleValidator;
	
	@Autowired
	private UserService userService;	
	
	@Autowired
	private SecurityService securityService;	

	@RequestMapping(value = "/article", method = RequestMethod.POST)
	public Article createArticle(
			@RequestHeader(value = MnsConstants.HEADER_AUTH, required = true) String authorization,
			@Valid Article article, BindingResult result) {

		logger.debug("Started processing create article request :: START");
		
		articleValidator.validate(article, result);
		if (result.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			for (ObjectError error : result.getAllErrors()) {
				sb.append(error.getCode());
				sb.append("\n");
			}
			throw new InvalidDataException(sb.toString());
		}
		
		String deToken = securityService.detokenize(authorization);
		long userId = userService.readMaker(article.getIdMaker()).getIdUser();
		if (userId != Long.parseLong(deToken)) {
			throw new AuthorizationException(MnsConstants.ERROR_INVALID_AUTHORIZATION);
		}
		
		Article articleRes = articleService.createArticle(article);
		
		return articleRes;
	}

	@RequestMapping(value = "/article/{articleId}", method = RequestMethod.PUT)
	public Article updateArticle(@PathVariable Long articleId, @RequestBody Article article, BindingResult result) {
		logger.debug("Updating article id {} with data ", articleId, article);
		if (article.getId() == 0) {
			throw new InvalidDataException("Article id should not be empty ");
		}
		if (articleId != article.getId()) {
			throw new InvalidDataException("Aricle id not matched with id from request body");
		}
		Article articleRes = articleService.updateArticle(articleId, article);
		return articleRes;
	}

	@RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
	public Article findArticle(@PathVariable("articleId") Long articleId) {
		if (articleId == null) {
			throw new InvalidDataException("Invalid article id to find articles");
		}
		Article articleRes = articleService.findArticle(articleId);
		if (articleRes == null) {
			throw new ResourceNotFoundException();
		}
		return articleRes;
	}

	@RequestMapping(value = "/article/search", method = RequestMethod.GET)
	public List<Article> searchArticle(@RequestParam(value = "idMaker", required = false) String idMaker,
			@RequestParam(value = "keywords", required = false) String keywords,
			@RequestParam(value = "serviceIds", required = false) List<String> serviceIds) {
		List<Article> articleRes = articleService.searchArticle(idMaker, keywords, serviceIds);
		return articleRes;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public void setArticleValidator(ArticleValidator articleValidator) {
		this.articleValidator = articleValidator;
	}

}
