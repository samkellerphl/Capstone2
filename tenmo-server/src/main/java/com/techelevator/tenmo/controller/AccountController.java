package com.techelevator.tenmo.controller;

import java.math.BigDecimal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;


@RestController 
public class AccountController {

	private AccountDAO accountDAO;

	public AccountController(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}


	@RequestMapping(path = "accounts/{id}/balance", method = RequestMethod.GET) 
	public BigDecimal getBalance(@PathVariable int id) {
		BigDecimal balance = accountDAO.getBalance(id);
		return balance;
	}

	@RequestMapping(path = "accounts/{userId}/updatebalance", method = RequestMethod.PUT)
	public BigDecimal addToBalance(@PathVariable int userId, @RequestBody BigDecimal amount) {
		BigDecimal balance = accountDAO.addToBalance(userId, amount);
		return balance;
	}

	@RequestMapping(path = "accounts/{userId}/newbalance", method = RequestMethod.PUT)
	public BigDecimal subtractFromBalance(@PathVariable int userId, @RequestBody BigDecimal amount) {
		BigDecimal balance = accountDAO.subtractFromBalance(userId, amount);
		return balance;
	}

	@RequestMapping(path = "accounts/{userId}", method = RequestMethod.GET) 
	public int getAccountIdByUserId(@PathVariable int userId) {
		int user = accountDAO.getAccountIdByUserId(userId);
		return user;
	}
}


