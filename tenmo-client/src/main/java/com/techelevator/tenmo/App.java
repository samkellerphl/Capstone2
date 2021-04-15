package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static String AUTH_TOKEN = "";
	private static String USERNAME = "";
	private static int ID = 0;

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private AccountService accountService;
	private TransferService transferService;

	// We connected our account and the transfer services 
	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), new TransferService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		//step 3: As an authenticated user of the system, I need to be able to see my Account Balance.
		try {
			BigDecimal balance = accountService.getBalanceByAccount(currentUser.getUser().getId()); // calling our getBalance method
			System.out.println("Current Balance: $" + String.format("%.2f", balance));   // formating our balance
		} catch (NullPointerException np) {
			System.out.println("No balance");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void viewTransferHistory() {
		// step 5:  As an authenticated user of the system, I need to be able to see transfers I have sent or received.
		// call our array
		Transfer[] listOfTransfers = transferService.getAllTransfers(currentUser.getUser().getId());
		// add those to an ArrayList
		List<Transfer> newTransferList = new ArrayList<>();

		System.out.println("-------------------------------------------");
		System.out.println("Transfers\nID\t\t From/To\t Amount");
		System.out.println("-------------------------------------------");

		// for each transfer in the array, add to our array list and draw their userId, the sender/recipient and amount
		for (Transfer i : listOfTransfers) {
			newTransferList.add(i);
			if (i.getToUser() == accountService.getAccountIdByUserId(currentUser.getUser().getId())) {
				System.out.println(i.getTransferId() + "\t\tFrom: " +  transferService.findUsernameByAccountId(i.getFromUser()) + "\t\t" + i.getAmount());
			}if (i.getFromUser() == accountService.getAccountIdByUserId(currentUser.getUser().getId())) {
				System.out.println(i.getTransferId() + "\t\tTo: " +  transferService.findUsernameByAccountId(i.getToUser()) + "\t\t" + i.getAmount());
			}
		}

		int newTransferId = console.getUserInputInteger("\nPlease enter transfer ID to view details (0 to cancel)");  // takes the input from user

		//check to see that the transfer id is valid 
		boolean validTransferId = false;

		// if it is, set it to valid
		for(Transfer transfer : newTransferList) {
			if (transfer.getTransferId() == (newTransferId)) {
				validTransferId = true;
			}

		}
		
		// if it is not, send them back to main menu
		while (!validTransferId) {
			System.out.println("Not a valid transfer ID");
			mainMenu();
		}

		// step 6
		// As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.
		// grabs the information for a singular transfer by transfer id to get the full details
	
		Transfer[] transferDetails = transferService.getTransferById(newTransferId);

		for (Transfer i : transferDetails) {

			System.out.println("\n-------------------------------------------");
			System.out.println("Transfer Details");
			System.out.println("-------------------------------------------\n");
			System.out.println("Id: " + i.getTransferId());
			System.out.println("From: " + transferService.findUsernameByAccountId(i.getFromUser()));
			System.out.println("To: " + transferService.findUsernameByAccountId(i.getToUser()));
			System.out.println("Type: " + transferService.getDescriptionByTypeId(i.getTransferTypeId()));
			System.out.println("Status: " + transferService.getDescriptionByStatusId(i.getTransferStatusId()));
			System.out.println("Amount: $" + String.format("%.2f", i.getAmount()));

		}
	}


	private void viewPendingRequests() {
		// step 8: As an authenticated user of the system, I need to be able to see my "pending" transfers.
		System.out.println("No current pending requests");
		mainMenu();

	}

	private void sendBucks() {
		//step 4:  As an authenticated user of the system, I need to be able to *send* a transfer of a specific amount of TE Bucks to a registered user.
		// grabs our array of users 
		User[] listOfUsers = transferService.getUsers();

		currentUser.getUser().getId();

		System.out.println("-------------------------------------------");
		System.out.println("Users\nID\t\t Name");
		System.out.println("-------------------------------------------");
		
		// step 4.1 
		// we want to parse that array into a new list
		List<User> newList = new ArrayList<>();
		
		// check to make sure the current user logged in does not appear in list
		for(User i : listOfUsers) {
			if(!currentUser.getUser().getId().equals(i.getId())) {
				System.out.println(i.getId() + "\t\t" + i.getUsername());
				newList.add(i);
			}
		}

		// prompt them for the user id
		int accountToId = console.getUserInputInteger("\nEnter ID of user you are sending to (0 to cancel)");
		
		// 0 cancels and returns to main menu
		if (accountToId == 0) {
			mainMenu();
		}
		
		// check to make sure the userid being entered is valid
		boolean validUserId = false;
		
		// if it is, set it to true
		for(User user : newList) {
			if (user.getId().equals(accountToId))
				validUserId = true;
		}
		// if it is not, prompt again
		while (!validUserId) {
			System.out.println("Please enter a valid user ID\n");
			accountToId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)\n");
			// check again after they were prompted
			for (User user : newList) {
				if (user.getId().equals(accountToId))
					validUserId = true;
			}
		}

		// prompt user for amount they wish to send, then parse into BD
		String amountToSendAsString = console.getUserInput("Enter Amount to Send");
		double amountToSendDouble = Double.parseDouble(amountToSendAsString);
		// make sure the user is actually sending money
		if (amountToSendDouble < 0) {
			System.out.println("You must send money!");
			// return to user prompt
			amountToSendAsString = console.getUserInput("Enter Amount to Send");
			amountToSendDouble = Double.parseDouble(amountToSendAsString);
			
		}
		
		
		BigDecimal bdamount = BigDecimal.valueOf(amountToSendDouble).setScale(2, RoundingMode.FLOOR);

		// step 4.5 - user cannot send more TE bucks than they have in their account
		if(accountService.getBalanceByAccount(currentUser.getUser().getId()).compareTo(bdamount) == -1) {
			System.out.println("You do not have enough money to send!");
			mainMenu();
		} else if // make sure the user is not trying to send money to themselves 
		(currentUser.getUser().getId() == accountToId) {
			System.out.println("You cannot send money to yourself"); 
		} else {

			// if the transfer goes through, we create a new transfer object and set the transfer information
			Transfer transfer = new Transfer();
			// step 4.2 a transfer includes user IDs of from & to, and the amount
			transfer.setAmount(BigDecimal.valueOf(amountToSendDouble));
			transfer.setToUser(accountService.getAccountIdByUserId(accountToId));
			transfer.setFromUser(accountService.getAccountIdByUserId(currentUser.getUser().getId()));
			// step 4.6 a sending transfer has an initial status of "Approved"
			transfer.setTransferStatusId(2);
			transfer.setTransferTypeId(2);
			// step 4.3 the receiver's balance is increased by amount of transfer
			accountService.addToBalance(accountToId, bdamount);
			// step 4.4 the sender's balance is decreased by amount of transfer
			accountService.subtractFromBalance(currentUser.getUser().getId(), bdamount);
			// we call our send transfer method
			transferService.sendTransfer(transfer);

			System.out.println("\nYou have sent: $" + bdamount);
		}
	}



	private void requestBucks() {
		// Step 7: As an authenticated user of the system, I need to be able to *request* a transfer of a specific amount of TE Bucks from another registered user.
		// Use Case 7
		System.out.println("Under Construction!");
		mainMenu();

	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) //will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch(AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
				App.AUTH_TOKEN = currentUser.getToken();
				App.USERNAME = currentUser.getUser().getUsername();
				App.ID = currentUser.getUser().getId();

			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
