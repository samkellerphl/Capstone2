package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {  //Methods necessary for our account table in the database

	BigDecimal getBalance(int userId);
	
	BigDecimal addToBalance(int userId, BigDecimal amount);
	
	BigDecimal subtractFromBalance(int userId, BigDecimal amount);
	
	int getAccountIdByUserId(int userId);
	
	int getUserIdByAccountId(int accountId);
	
}

