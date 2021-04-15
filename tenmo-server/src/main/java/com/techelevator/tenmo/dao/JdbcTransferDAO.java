package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@Component
public class JdbcTransferDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;
	private AccountDAO accountDAO;

	public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// updating our database for sending a transfer
	@Override
	public void sendTransfer(Transfer transfer) {
		String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?)";
		try {
			jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getFromUser(), transfer.getToUser(), transfer.getAmount()); 
		} catch (RestClientException rcEx) {
			System.out.println(rcEx.getLocalizedMessage());
		}
	}

	// retrieving a list of users
	public List<User> getUsers() {
		List<User> transferList = new ArrayList<>();
		String sql = "SELECT user_id, username FROM users";
		try {
			SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
			while(results.next()) {
				User user = mapRowToUser(results);
				transferList.add(user);
			}
			return transferList;
		}
		catch (RestClientException rcEx) {
			System.out.println(rcEx.getLocalizedMessage());
		}
		return transferList;
	}

	// retrieving a list of transfers by userId
	@Override
	public List<Transfer> getTransfers(int userId) {
		List<Transfer> list = new ArrayList<>();
		String sql = "SELECT t.* FROM transfers t"                                                   
				+ " JOIN accounts a ON (a.account_id = t.account_from) OR (a.account_id = t.account_to)"
				+" WHERE a.user_id = ?"; 
		try {
			SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
			while(results.next()) {
				Transfer transfer = mapRowToTransfer(results);
				list.add(transfer);
			}
			return list;
		} catch (RestClientException rcEx) {
			System.out.println(rcEx.getLocalizedMessage());
		}
		return list;
	}

	// retrieving transfer info by transferId
	@Override
	public List<Transfer> getTransferById(int transferId) {
		List<Transfer> newList = new ArrayList<>();
		String sql = "SELECT * FROM transfers t WHERE t.transfer_id = ?";
		try {
			SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

			while(results.next()) {
				Transfer transfer = mapRowToTransfer(results);
				newList.add(transfer);
			}
			return newList;
		} catch (RestClientException rcEx) {
			System.out.println(rcEx.getLocalizedMessage());
		}
		return newList;
	}

	// method to find a transfer type description string
	@Override
	public String getDescriptionByTypeId(int transferTypeId) {
		String sql = "SELECT transfer_type_desc FROM transfer_types WHERE transfer_type_id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, String.class, transferTypeId);
		} catch (RestClientException rcEx) {
			System.out.println(rcEx.getLocalizedMessage());
		}
		return null;
	}

	// method to find a transfer status description string
	@Override
	public String getDescriptionByStatusId(int transferStatusId) {
		String sql = "SELECT transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, String.class, transferStatusId);
		} catch (RestClientException rcEx) {
			System.out.println(rcEx.getLocalizedMessage());
		}
		return null;
	}

	// map to user table
	public User mapRowToUser(SqlRowSet rs) {
		User user = new User();
		user.setId(rs.getLong("user_id"));
		user.setUsername(rs.getString("username"));;
		return user;
	}

	// map to transfer table
	public Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer transfer = new Transfer();
		transfer.setTransferId(results.getInt("transfer_id"));
		transfer.setTransferTypeId(results.getInt("transfer_type_id"));
		transfer.setTransferStatusId(results.getInt("transfer_status_id"));
		transfer.setToUser(results.getInt("account_to"));
		transfer.setFromUser(results.getInt("account_from"));
		transfer.setAmount(results.getBigDecimal("amount"));
		return transfer;
	}


}
	 