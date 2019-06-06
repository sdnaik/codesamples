package com.mns.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mns.constants.DbQueries;
import com.mns.dto.Article;
import com.mns.dto.Maker;
import com.mns.exceptions.ServerException;

@Repository
public class ArticleDao {

	private static final Logger logger = LoggerFactory.getLogger(ArticleDao.class);

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public Article createArticle(final Article article) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		logger.debug("Create article sql: " + DbQueries.CREATE_ARTICLE);

		try {

			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(DbQueries.CREATE_ARTICLE, new String[] { "id" });
					ps.setLong(1, article.getIdMaker());
					ps.setString(2, article.getKeywords());
					ps.setString(3, article.getTitle());
					ps.setString(4, article.getFormat());
					ps.setString(5, article.getFileName());
					ps.setString(6, article.getLink());
					ps.setBoolean(7, article.isPublished());
					ps.setInt(8, article.getHelpfulCount());
					ps.setInt(9, article.getViewCount());
					Date dateInsert = article.getDateInsert();
					if (dateInsert != null) {
						ps.setDate(10, new java.sql.Date(dateInsert.getTime()));
					} else {
						ps.setDate(10, null);
					}
					Date dateUpdate = article.getDateUpdate();
					if (dateUpdate != null) {
						ps.setDate(11, new java.sql.Date(dateUpdate.getTime()));
					} else {
						ps.setDate(11, null);
					}
					ps.setString(12, article.getDbUser());
					return ps;
				}
			}, keyHolder);
		} catch (DataAccessException e) {
			logger.error("Error while creating the article. " + e);
			throw new ServerException("Create article exception", e);
		}
		return findArticle((Long) keyHolder.getKey());
	}

	public void createArticleXService(final Article article) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		logger.debug("Create articleXService sql: " + DbQueries.CREATE_ARTICLEX_SERVICE);
		List<String> serviceIds = article.getServiceIds();

		try {
			for (final String idservice : serviceIds) {
				jdbcTemplate.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(DbQueries.CREATE_ARTICLEX_SERVICE,
								new String[] { "id" });
						ps.setLong(1, article.getId());
						ps.setString(2, idservice);
						Date dateInsert = article.getDateInsert();
						if (dateInsert != null) {
							ps.setDate(3, new java.sql.Date(dateInsert.getTime()));
						} else {
							ps.setDate(3, null);
						}
						Date dateUpdate = article.getDateUpdate();
						if (dateUpdate != null) {
							ps.setDate(4, new java.sql.Date(dateUpdate.getTime()));
						} else {
							ps.setDate(4, null);
						}
						ps.setString(5, article.getDbUser());
						return ps;
					}
				}, keyHolder);
			}
			logger.debug("Creating article successfully completed");
		} catch (DataAccessException e) {
			logger.error("Error while creatingXService the article. " + e);
			throw new ServerException("Create articleXService exception", e);
		}
	}

	public Article updateArticle(Long articleId, Article article) {

		findArticle(articleId);

		StringBuilder sql = new StringBuilder("update article set ");
		List<String> paramsFileds = new ArrayList<String>();
		List<Object> paramsList = new ArrayList<Object>();
		
		try {
		
			int helpfulCount = article.getHelpfulCount();
			if (helpfulCount != 0) {
				paramsFileds.add("helpful_count = ?");
				paramsList.add(helpfulCount);
			}
			
			int viewCount = article.getViewCount();
			if (viewCount != 0) {
				paramsFileds.add("view_count = ?");
				paramsList.add(viewCount);
			}

			/*
			This method is primarily used for updating the view count; 
			published parameter is set manually by the admin (after review) as of 12/2008 and hence removing updates to that parameter
			 */
			
			/*
			boolean published = article.isPublished();
			paramsFileds.add("published = ?");
			paramsList.add(published);
			*/
			if(paramsFileds.size() > 0) {
				sql.append(StringUtils.join(paramsFileds, ","));	
			}
			sql.append(" where id_article = ?");
			paramsList.add(articleId);
			
			logger.debug("update sql = " + sql.toString());

			logger.debug("Update article parameters");
			int paramsListCounter = 0;
			for (Object param : paramsList) {
				paramsListCounter++;
				logger.debug(paramsListCounter + "->" + param.toString());
			}

			int updated = 0; 
			
			//execute update only if you have anything to update
			if(paramsFileds.size() > 0){
				updated = this.jdbcTemplate.update(sql.toString(), paramsList.toArray());	
			}
			logger.info("Updated article completed status : " + updated);

		} catch (DataAccessException e) {
			logger.error("Error while updating review. " + e);
			throw new ServerException("Update review exception ", e);
		}

		return findArticle(articleId);
	}

	public void updateArticleCountInMaker(Long idMaker) {
		Maker maker = userDAO.readMaker(idMaker);
		int updatedArticleCount = maker.getArticleCount() + 1;
		try {
			jdbcTemplate.update(DbQueries.UPDATE_ARTICLE_COUNT_IN_MAKER, new Object[] { updatedArticleCount, idMaker });
			logger.debug("Successfully updated article count in makers table");
		} catch (DataAccessException e) {
			logger.error("error while updating article count in maker: " + idMaker);
			throw new ServerException("error while updating article count in maker: " + idMaker);
		}
	}

	public Article findArticle(Long articleId) {
		logger.debug("find article by id sql: " + DbQueries.FIND_ARTICLE_BY_ID);
		logger.debug("find article with id : " + articleId);
		Article article;
		try {
			article = this.jdbcTemplate.queryForObject(DbQueries.FIND_ARTICLE_BY_ID, new Object[] { articleId },
					new RowMapper<Article>() {
						public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
							Article articleRes = new Article();
							articleRes.setId(rs.getLong("id_article"));
							articleRes.setIdMaker(rs.getLong("idmaker"));
							articleRes.setKeywords(rs.getString("keywords"));
							articleRes.setTitle(rs.getString("title"));
							articleRes.setFileName(rs.getString("filename"));
							articleRes.setFormat(rs.getString("format"));
							articleRes.setLink(rs.getString("link"));
							articleRes.setPublished(rs.getBoolean("published"));
							articleRes.setHelpfulCount(rs.getInt("helpful_count"));
							articleRes.setViewCount(rs.getInt("view_count"));
							articleRes.setDateInsert(rs.getDate("dateInsert"));
							articleRes.setDateUpdate(rs.getDate("dateUpdate"));
							articleRes.setDbUser(rs.getString("dbuser"));
							String serviceIdsDb = rs.getString("serviceIds");
							if (serviceIdsDb != null) {
								articleRes.setServiceIds(Arrays.asList(StringUtils.split(serviceIdsDb, ",")));
							}
							articleRes.setUserLogoFile(rs.getString("logofile"));
							articleRes.setUserName(rs.getString("name"));
							return articleRes;
						}
					});
			logger.debug("Successfully found articles with id {}", articleId);
		} catch (EmptyResultDataAccessException ex) {
			throw new ServerException("No records found for the article id: " + articleId);
		} catch (DataAccessException e) {
			logger.error("error while finding the article data with id {} and error {}.", articleId, e);
			throw new ServerException("Read article exception", e);
		}
		return article;
	}

	public List<String> findServiceIdsFromArticleXService(Long articleId) {
		logger.debug("read article x service ids sql: " + DbQueries.READ_SERVICE_IDS_ARTICLE_X_SERVICE);
		List<String> serviceIdList = new ArrayList<>();
		try {
			serviceIdList = jdbcTemplate.queryForList(DbQueries.READ_SERVICE_IDS_ARTICLE_X_SERVICE,
					new Object[] { articleId }, String.class);
			logger.debug("Successfully found service id list for article Id {}", articleId);
		} catch (Exception e) {
			logger.error("error while finding service ids from articlexservice.", e);
			throw new ServerException();
		}
		return serviceIdList;
	}

	public List<Article> searchArticle(String idMaker, String keywords, List<String> serviceIds) {
		logger.debug("Search articles entered with idmaker {}, keywords {}, serviceids {}", idMaker, keywords,
				serviceIds);
		List<Article> articleList = new ArrayList<>();
						
		StringBuilder sql = new StringBuilder(DbQueries.SEARCH_ARTICLES);
		List<String> paramsFileds = new ArrayList<String>();
		List<Object> paramsList = new ArrayList<Object>();
		try {
			if (idMaker != null) {
				try {
					Long idMakerVal = Long.valueOf(idMaker);
					paramsFileds.add(" a.idmaker = ?");
					paramsList.add(idMakerVal);
				} catch (NumberFormatException ex) {
					throw new ServerException("Invalid idmaker format " + idMaker);
				}
			}
			if (serviceIds != null && !serviceIds.isEmpty()) {
				StringBuilder builder = new StringBuilder();
				builder.append(" idservice in (");
				builder.append(StringUtils.join(serviceIds.toArray(), ","));
				builder.append(")");
				paramsFileds.add(builder.toString());
			}
			if (!paramsFileds.isEmpty()) {
				sql.append(" and ");
				sql.append(StringUtils.join(paramsFileds, " and "));
			}
			
			List<String> comaSplitKeywords = null; 
			List<String> separatedKeywords = new ArrayList<String>();
			
			if (keywords!=null && !keywords.isEmpty()) {
			
				comaSplitKeywords = Arrays.asList(StringUtils.split(keywords, ','));
				
				if (comaSplitKeywords != null) {
					for (String comaSplitKeyword : comaSplitKeywords) {
							List<String> spaceSplitKeywords = new ArrayList<String>();
							if(comaSplitKeyword != null && !comaSplitKeyword.isEmpty()) {
								spaceSplitKeywords = Arrays.asList(StringUtils.split(comaSplitKeyword, ' '));
							}
							separatedKeywords.addAll(spaceSplitKeywords);
					}			
				}				

				if (separatedKeywords != null && separatedKeywords.size() > 0) {
					sql.append(" and ( ");
					for (int i=0; i<separatedKeywords.size(); i++) {
						String keyword = separatedKeywords.get(i);
						if(keyword != null && !keyword.isEmpty()){
							sql.append(" keywords like '%" + keyword + "%'");
						}
						if (i != separatedKeywords.size() - 1) {
							sql.append(" or ");
						}
					}
					sql.append(" ) ");
				}

			}
			
			sql.append(DbQueries.GROUPBY_ARTICLE_ID);

			logger.debug("search article sql = " + sql.toString());

			logger.debug("Search article parameters");

			int paramsListCounter = 0;
			for (Object param : paramsList) {
				paramsListCounter++;
				logger.debug(paramsListCounter + "->" + param.toString());
			}

			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql.toString(), paramsList.toArray());

			for (Map<String, Object> map : queryForList) {

				Article articleRes = new Article();
				if (map.get("id_article") != null) {
					articleRes.setId((Integer) map.get("id_article"));
				}
				if (map.get("idmaker") != null) {
					articleRes.setIdMaker((Integer) map.get("idmaker"));
				}
				articleRes.setKeywords((String) map.get("keywords"));
				articleRes.setTitle((String) map.get("title"));
				articleRes.setFileName((String) map.get("filename"));
				articleRes.setLink((String) map.get("link"));
				if (map.get("published") != null) {
					int published = (Integer) map.get("published");
					if (published != 0) {
						articleRes.setPublished(true);
					} else {
						articleRes.setPublished(false);
					}
				}
				if (map.get("helpful_count") != null) {

					articleRes.setHelpfulCount((Integer) map.get("helpful_count"));
				}
				if (map.get("view_count") != null) {

					articleRes.setViewCount((Integer) map.get("view_count"));
				}
				if (map.get("dateInsert") != null) {

					articleRes.setDateInsert((Date) map.get("dateInsert"));
				}
				if (map.get("dateUpdate") != null) {

					articleRes.setDateUpdate((Date) map.get("dateUpdate"));
				}
				articleRes.setDbUser((String) map.get("dbuser"));

				if (map.get("serviceids") != null) {
					String serviceIdsDb = (String) map.get("serviceids");
					articleRes.setServiceIds(Arrays.asList(StringUtils.split(serviceIdsDb, ",")));
				}
				
				if (map.get("logofile") != null) {
					articleRes.setUserLogoFile((String) map.get("logofile"));
				}
				
				if (map.get("name") != null){
					articleRes.setUserName((String) map.get("name"));
				}

				articleList.add(articleRes);

			}
			logger.debug("Search_Article_Results={}", articleList.size());
		} catch (EmptyResultDataAccessException e) {
			logger.error("No results found for the search article service", e);
			throw new ServerException("No results for the given criteria,Please try with different input;");
		} catch (DataAccessException ex) {
			logger.error("Data access exception", ex);
			throw new ServerException("Data access exception while search article service call", ex);
		}

		return articleList;
	}

}
