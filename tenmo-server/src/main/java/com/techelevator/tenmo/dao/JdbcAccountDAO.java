package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;


import com.techelevator.tenmo.model.Account;

@Component 
public class JdbcAccountDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;

	public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	// our get balance method selects balance from the account table based on the user_id
	@Override
	public BigDecimal getBalance(int userId) {
		BigDecimal currentBalance = new BigDecimal(0);
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";
		try {
			BigDecimal results = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
			return results;
		} catch (RestClientException ex) {
			System.out.println("error!");
		}
		return currentBalance;
	}

	// the below methods update a user's balance in the account table 
	public BigDecimal addToBalance(int userId, BigDecimal amount) {
		BigDecimal bdbalance = new BigDecimal(0);
		String sql = "UPDATE accounts SET balance = (balance + ?) WHERE user_id = ?";
		try {
			int balance = jdbcTemplate.update(sql, amount, userId);
			bdbalance = BigDecimal.valueOf(balance);
			return bdbalance;	
		} catch (RestClientException ex) {
			System.out.println("error!");
		}
		return bdbalance;

	}

	public BigDecimal subtractFromBalance(int userId, BigDecimal amount) {
		BigDecimal bdbalance = new BigDecimal(0);
		String sql = "UPDATE accounts SET balance = (balance - ?) WHERE user_id = ?";
		try {
			int balance = jdbcTemplate.update(sql, amount, userId);
			bdbalance = BigDecimal.valueOf(balance);
			return bdbalance;	
		} catch (RestClientException ex) {
			System.out.println("error!");
		}
		return bdbalance;

	}

	// this method finds an account id by the user id
	@Override
	public int getAccountIdByUserId(int userId) {
		int results = 0;
		String sql = "SELECT account_id FROM accounts WHERE user_id = ?";
		try {	
			results = jdbcTemplate.queryForObject(sql, Integer.class, userId);
			return results; 
		} catch (RestClientException ex) {
			System.out.println("error!");
		}
		return results;
	}

	// this method finds a user id by the account id
	@Override
	public int getUserIdByAccountId(int accountId) {
		int results = 0;
		String sql = "SELECT user_id FROM accounts WHERE account_id = ?";
		try {
			results = jdbcTemplate.queryForObject(sql, Integer.class, accountId);
			return results;
		} catch (RestClientException ex) {
			System.out.println("error!");
		}
		return results;
	}

	// the map
	private Account mapRowToAccount(SqlRowSet results) {
		Account a = new Account();
		a.setAccountId(results.getInt("account_id"));
		a.setBalance(results.getBigDecimal("balance"));
		a.setUserId(results.getInt("user_id"));
		return a;

	}
}
