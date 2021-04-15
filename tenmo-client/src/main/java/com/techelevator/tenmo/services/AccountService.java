package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;




public class AccountService {  //connect everything built in server

	public static String AUTH_TOKEN = "";
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	 

	public AccountService(String url) {
		this.BASE_URL = url;

	}

	public BigDecimal getBalanceByAccount(int id) {
		BigDecimal balance = new BigDecimal(0);
		try {
			balance = restTemplate.exchange(BASE_URL + "accounts/"  + id + "/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();	
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return balance;
	}

	public BigDecimal addToBalance(int userId, BigDecimal amount) {
		BigDecimal balance = new BigDecimal(0);
		try {
			return balance = restTemplate.exchange(BASE_URL + "accounts/" + userId + "/updatebalance", HttpMethod.PUT, makeAccountEntity(amount), BigDecimal.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return balance;
	}

	public BigDecimal subtractFromBalance(int userId, BigDecimal amount) {
		BigDecimal balance = new BigDecimal(0);
		try {
			return balance = restTemplate.exchange(BASE_URL + "accounts/" + userId + "/newbalance", HttpMethod.PUT, makeAccountEntity(amount), BigDecimal.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return balance;

	}

	public int getAccountIdByUserId(int userId) {
		try {
			int accountByUserId = restTemplate.exchange(BASE_URL + "accounts/" + userId, HttpMethod.GET, makeAuthEntity(), Integer.class).getBody();
			return accountByUserId;
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return 0;

	}

	public int getUserIdByAccountId(int accountId) {
		try {
			int userIdByAccountId = restTemplate.exchange(BASE_URL + "accounts/" + accountId, HttpMethod.GET, makeAuthEntity(), Integer.class).getBody();
			return userIdByAccountId;
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return 0;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

	// new entity for the update
	private HttpEntity<BigDecimal> makeAccountEntity(BigDecimal amount) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<BigDecimal> entity = new HttpEntity<>(amount, headers);
		return entity;
	}
}
