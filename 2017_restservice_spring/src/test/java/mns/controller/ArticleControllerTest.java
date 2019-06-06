package mns.controller;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.mns.controller.ArticleController;
import com.mns.dto.Article;
import com.mns.service.ArticleService;
import com.mns.validators.ArticleValidator;

@RunWith(MockitoJUnitRunner.class)
public class ArticleControllerTest {

	private ArticleController articleController;

	private ArticleValidator articleValidator;

	private ArticleService articleService;

	private Article article;

	@Before
	public void setUp() {
		articleController = new ArticleController();
		articleValidator = Mockito.mock(ArticleValidator.class);
		articleService = Mockito.mock(ArticleService.class);
		articleController.setArticleValidator(articleValidator);
		articleController.setArticleService(articleService);

		article = new Article();
		article.setIdMaker(1l);
	}
	

	@Test
	public void createArticle() {
		//authorization header needs to be applied to make this work
		/*
		BindingResult result = Mockito.mock(BindingResult.class);
		BDDMockito.given(result.hasErrors()).willReturn(false);
		BDDMockito.given(articleService.createArticle(article)).willReturn(article);
		Article articleRes = articleController.createArticle(article, result);
		assertNotNull(articleRes);
		*/
	}
	
	public void creatArticleIT() {
		Article article  = new Article();
		article.setDateInsert(new Date());
		article.setFileName("test");
		article.setFormat("pdf");
		article.setHelpfulCount(1);
		article.setKeywords("article, blog, tutorial");
		article.setTitle("Mybio");
		article.setViewCount(1);
		//Article articleRes = articleController.createArticle(article, result)
	}
}
