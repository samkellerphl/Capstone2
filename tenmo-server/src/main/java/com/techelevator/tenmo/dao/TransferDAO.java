package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO { // Methods needed for the transfers

	void sendTransfer(Transfer transfer);

	List<Transfer> getTransfers(int userId);

	List<Transfer> getTransferById(int transferId);

	String getDescriptionByTypeId(int transferTypeId);

	String getDescriptionByStatusId(int transferStatusId);

}
