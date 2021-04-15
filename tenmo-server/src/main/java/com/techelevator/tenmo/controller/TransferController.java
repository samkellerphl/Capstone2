package com.techelevator.tenmo.controller;


import java.util.ArrayList;
import java.util.List;



import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

//@PreAuthorize("permitAll") 
@RestController
public class TransferController {

	private TransferDAO transferDAO;
	private UserDAO userDAO; 


	public TransferController(TransferDAO transferDAO, UserDAO userDAO) {
		this.transferDAO = transferDAO;	
		this.userDAO = userDAO;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "transfers", method = RequestMethod.POST)
	public void sendTransfer(@RequestBody Transfer transfer) {
		transferDAO.sendTransfer(transfer);
	}

	@RequestMapping(path = "users", method = RequestMethod.GET)
	public List<User> getUsers() {
		List<User> listOfUsers = new ArrayList<>();
		listOfUsers = userDAO.findAll();
		return listOfUsers;
	}

	@RequestMapping(path = "transfers/{userId}", method = RequestMethod.GET)
	public List<Transfer> getTransfers(@PathVariable int userId) {
		List<Transfer> results = new ArrayList<>();
		results = transferDAO.getTransfers(userId);
		return results;
	}

	@RequestMapping(path = "accounts/transfers/{transferId}", method = RequestMethod.GET) 
	public List<Transfer> getTransferById(@PathVariable int transferId) {
		List<Transfer> results = new ArrayList<>();
		results = transferDAO.getTransferById(transferId);
		return results;

	}

	@RequestMapping(path = "users/{accountId}", method = RequestMethod.GET)
	public String findUsernameByAccountId(@PathVariable int accountId) {
		String user = userDAO.findUsernameByAccountId(accountId);
		return user;
	}


	@RequestMapping(path = "transfers/type/{transferTypeId}", method = RequestMethod.GET)
	public String getDescriptionByTypeId(@PathVariable int transferTypeId){
		return transferDAO.getDescriptionByTypeId(transferTypeId); 
	}

	@RequestMapping(path = "transfers/status/{transferStatusId}", method = RequestMethod.GET)
	public String getDescriptionByStatusId(@PathVariable int transferStatusId){
		return transferDAO.getDescriptionByStatusId(transferStatusId); 
	}


}
