package com.mns.service;

import java.util.List;

import com.mns.dto.Article;

public interface ArticleService {

	public Article createArticle(Article article);

	public Article updateArticle(Long articleId, Article article);

	public Article findArticle(Long articleId);

	public List<Article> searchArticle(String idMaker, String keywords, List<String> serviceIds);

}
