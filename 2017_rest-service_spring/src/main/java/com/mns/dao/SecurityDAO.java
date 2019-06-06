package com.mns.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mns.constants.DbQueries;
import com.mns.dto.Maker;
import com.mns.dto.User;
import com.mns.exceptions.ServerException;

@Repository
public class SecurityDAO {
	
	static final Logger logger = LoggerFactory.getLogger(SecurityDAO.class);
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public User authenticate(String email, String password) {

		User user = null;
		
		try {
			user = this.jdbcTemplate.queryForObject(DbQueries.READ_USER_FOR_LOGIN, new Object[] { email, password },
					new RowMapper<User>() {
						public User mapRow(ResultSet rs, int rowNum) throws SQLException {
							Maker maker = new Maker();
							User user = new User();
							user.setMaker(maker);
							user.setIdUser(rs.getLong("iduser"));
							user.setName(rs.getString("name"));
							user.setContactAddress1(rs.getString("contactaddress1"));
							user.setContactAddress2(rs.getString("contactaddress2"));
							user.setContactCity(rs.getString("contactcity"));
							user.setContactState(rs.getString("contactstate"));
							user.setContactCountry(rs.getString("contactcountry"));
							user.setContactZip(rs.getString("contactzip"));
							user.setPhone1(rs.getString("phone1"));
							user.setPhone2(rs.getString("phone2"));
							user.setEmail(rs.getString("email"));
							user.setMakerType(rs.getBoolean("makertype"));
							user.setSaverType(rs.getBoolean("savertype"));
							maker.setIdMaker(rs.getLong("idmaker"));
							maker.setLogoFile(rs.getString("logofile"));
							maker.setDescription(rs.getString("description"));
							maker.setWebsite(rs.getString("website"));
							maker.setViewCount(rs.getInt("viewcount"));
							maker.setContactRequestCount(rs.getInt("contactrequestcount"));
							maker.setArticleCount(rs.getInt("articlecount"));
							maker.setReviewCount(rs.getInt("reviewcount"));
							maker.setReviewRank(rs.getInt("reviewrank"));
							maker.setServiceIds(rs.getString("serviceids"));
							return user;
						}
					});
		} catch (Exception e) {
			logger.error("error while accessing the user data.", e);
			throw new ServerException();
		}
		if (user.getIdUser() <= 0){
			user = null;
		}
		return user;
		
	}
	
	
	public boolean doesUserExist (String email) {
		boolean exists = false;
		logger.debug(" input email = " + email);
		try {
			Integer rowCount = this.jdbcTemplate.queryForObject(DbQueries.READ_EMAIL, Integer.class, email);
			logger.debug("query executed is " +  DbQueries.READ_EMAIL);
			logger.debug("email search returned " + rowCount + " results" );
			if (rowCount > 0) exists = true;			
		} catch (Exception e) {
			logger.error("error occured while verifying email address", e);
		}
		return exists;
	}
	
	public String getUserPassword(String email){
		String password = "";
		logger.debug(" input email = " + email);
		try {
			 password = this.jdbcTemplate.queryForObject(DbQueries.READ_PASSWORD, String.class, email);
			logger.debug("query executed is " +  DbQueries.READ_PASSWORD);					
		} catch (Exception e) {
			logger.error("error occured while retrieving password", e);
		}
		return password;
	}

	
}
