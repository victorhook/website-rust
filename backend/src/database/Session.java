package database;

import java.util.*;
import java.util.StringTokenizer;

public class Session implements Runnable {

	private final int ID;
	private final Scanner SCANNER = new Scanner(System.in);
	private boolean sessionAlive;
	private final String CREATE = "create", DELETE = "delete", RENAME = "rename";
	
	private Database database;
	
	Session(Database database, int id) {
		this.ID = id;
		this.database = database;
		sessionAlive = true;
	}
	
	public static void main(String[] args) {
		new Thread(new Session(new Database(), 1)).start();
	}
	
	private void showDatabase() {
		
	}
	
	@Override
	public void run() {
		
		StringTokenizer tokenizer;
		String command = "";
		 
		while (sessionAlive) {
		
			tokenizer = new StringTokenizer(SCANNER.nextLine());
			
			while (tokenizer.hasMoreTokens()) {
				
				command = tokenizer.nextToken();
				
				// If the command is quit or exit, the sessions is over
				sessionAlive = (command.equals("quit") || command.equals("exit")) ? false : true;

			}

		}
		
	}
 	
}
