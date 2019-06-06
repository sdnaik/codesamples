package com.mns.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mns.constants.MnsConstants;
import com.mns.dao.ArticleDao;
import com.mns.dto.Article;
import com.mns.exceptions.ServerException;
import com.mns.service.ArticleService;
import com.mns.util.FileConvertUtil;

@Service
public class ArticleServiceImpl implements ArticleService {

	private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
	@Autowired
	private ArticleDao articleDao;

	@Override
	@Transactional
	public Article createArticle(Article article) {
		List<MultipartFile> multipartFiles = article.getAttachments();
		boolean isFilesAvailable = false;
		if (!multipartFiles.isEmpty()) {
			isFilesAvailable = true;
		}
		if (isFilesAvailable) {
			article.setFileName(FileConvertUtil.getFileName(multipartFiles));
			article.setFormat(FileConvertUtil.getFileFormat(multipartFiles));
		}
		article.setDateInsert(new Date());
		Article articleDb = articleDao.createArticle(article);
		Long articleId = articleDb.getId();
		articleDb.setServiceIds(article.getServiceIds());
		articleDao.createArticleXService(articleDb);
		addServiceIds(articleDb);
		articleDao.updateArticleCountInMaker(articleDb.getIdMaker());
		if (isFilesAvailable) {
			saveFilesToServer(articleId, multipartFiles);
			articleDb.setAttachments(null);
		}

		return articleDb;
	}

	@Override
	@Transactional
	public Article updateArticle(Long articleId, Article article) {
		article.setDateUpdate(new Date());
		Article articleDb = articleDao.updateArticle(articleId, article);
		addServiceIds(articleDb);
		return articleDb;
	}

	@Override
	public Article findArticle(Long articleId) {
		Article articleRes = articleDao.findArticle(articleId);
		return articleRes;
	}

	@Override
	public List<Article> searchArticle(String idMaker, String keywords, List<String> serviceIds) {
		List<Article> articlesRes = articleDao.searchArticle(idMaker, keywords, serviceIds);
		return articlesRes;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	public void saveFilesToServer(long articleId, List<MultipartFile> multiPartFiles) throws ServerException {
		String filePath = MnsConstants.ARTICLE_FILE_PATH + articleId;
		logger.debug("saving articles at path: " + filePath);
		FileConvertUtil.saveMultipartFilesToServer(multiPartFiles, filePath);
	}

	public Article addServiceIds(Article article) {
		List<String> serviceIds = articleDao.findServiceIdsFromArticleXService(article.getId());
		article.setServiceIds(serviceIds);
		return article;
	}
}
