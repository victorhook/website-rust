package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

class Database {

	private File passwd = new File(".passwd");
	private File users = new File(passwd, "users");
	private File nameFile = new File(passwd, "name_ID_");
	private File passFile = new File(passwd, "pass_ID_");
	
	private Crypter crypter;
	private int totalUsers;
	
	Database() {
		crypter = new Crypter();
		totalUsers = 0;
		init();
		addNewUser("victor".toCharArray(), "pelleson".toCharArray());
		getUser();
	}
	
	public static void main(String[] args) {
		Database db = new Database();
	}
	 
	private void init() {
		// Initializes the database, makes sure all files and folders that should exist exists
		if (!passwd.exists()) {
			passwd.mkdir();
		}
		if (!users.exists()) {
			try {
				users.createNewFile();			// passwd file
			} catch (Exception e) {}
		}
		
	}
	
	private void getUser() {
		try {
			FileInputStream reader = new FileInputStream(users);
			
			byte[] buffer = new byte[(int) users.length()];
			reader.read(buffer);
			
			byte[] crypted = crypter.decrypt(buffer);
			System.out.println(new String(crypted, "UTF8"));
			
		} catch (Exception e) {}
	}
	
	private void addNewUser(char[] username, char[] password) {
		
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(users, true));
			writer.println("ID" + totalUsers++);
			writer.close();
			
			FileOutputStream writer = new FileOutputStream(new File(nameFile, totalUsers), true);
			writer.write((byte) totalUsers++);
			writer.write(":".getBytes());
			writer.write(crypter.encrypt(username));
			writer.write(":".getBytes());
			writer.write(crypter.encrypt(password));
			writer.write("\n".getBytes());
			writer.close();
			
		} catch (Exception e) {}
		
		
	}
	
}
