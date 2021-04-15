package com.techelevator.tenmo.services;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class TransferService {

	public static String AUTH_TOKEN = "";
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	public TransferService(String url) {
		this.BASE_URL = url;
	}


	public User[] getUsers() {
		User[] listOfUsers = null;
		try {
			return listOfUsers = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return listOfUsers;
	}

	public void sendTransfer(Transfer transfer) {
		try {
			restTemplate.exchange(BASE_URL + "transfers", HttpMethod.POST, makeTransferEntity(transfer), Transfer.class); 
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
	}

	public Transfer[] getAllTransfers(int userId) {
		try {
			Transfer[] listOfTransfers = restTemplate.exchange(BASE_URL + "transfers/" + userId,  HttpMethod.GET, makeAuthEntity() , Transfer[].class).getBody();
			return listOfTransfers;
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return null;
	}

	public Transfer[] getTransferById(int transferId) {
		try {
			Transfer[] transferById = restTemplate.exchange(BASE_URL + "accounts/transfers/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
			return transferById;
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return null;
	}


	public String findUsernameByAccountId(int accountId) {
		try {
			String usernameByAccount = restTemplate.exchange(BASE_URL + "users/" + accountId, HttpMethod.GET, makeAuthEntity() , String.class).getBody();
			return usernameByAccount;
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return null;
	}


	public String getDescriptionByTypeId(int transferTypeId) {
		try {
			return restTemplate.exchange(BASE_URL + "transfers/type/" + transferTypeId, HttpMethod.GET, makeAuthEntity() , String.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return null;
	}

	public String getDescriptionByStatusId(int transferStatusId) {
		try {
			return restTemplate.exchange(BASE_URL + "transfers/status/" + transferStatusId, HttpMethod.GET, makeAuthEntity() , String.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return null;
	}

	// our transfer entity 
	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers); 
		return entity;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

}
