package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Database {

	// Database file tree 
	//		DATABASE
	//			-> DOCUMENTS
	//				-> COURSE
	//					-> CHAPTER FILES
	
	private final File DOCUMENTS = new File("documents");
	private ArrayList<Course> courses;
	
	private File passwd = new File(".passwd");
	private File users = new File(passwd, "users");
	private final String NAME_FILE = ".passwd/name_ID_";
	private final String PASS_FILE = ".passwd/pass_ID_";
	private final int USER_DOESNT_EXIST = 0, WRONG_PASSWORD = 1, LOGIN_OK = 2;
	
	private Crypter crypter;
	private int totalUsers;
	
	Database() {
		crypter = new Crypter();
		init();
		

	}
	
	public static void main(String[] args) {
		Database db = new Database();
	}
	
	
	// Methods that regard session handling
	
	boolean courseNameOk(String courseName) {
		// Returns false if a course name already exists
		for (Course course : courses) {
			if (course.getName().equals(courseName)) {
				return false;
			}
		}
		return true;
	}
	
	void createCourse(String courseName) {
		// Adds a new course
		courses.add(new Course(DOCUMENTS, courseName));
		
	}
	
	Course getCourse(int courseNumber) {
		return courses.get(courseNumber);
	}
	
	boolean chapterOk(Course course, int chapter, String chapterName) {
		// Ensures that the course doesn't contain any chapters with the
		// same name or number
		return course.containsChapter(chapter, chapterName);
	}
	
	void createChapter(Course course, int chapter, String chapterName) {
		// Adds a new chapter to the course
		course.createChapter(chapter, chapterName);
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
		if (users.exists()) {
			try {
				// Reads the total user ammount 
				Scanner scan = new Scanner(users);
				totalUsers = scan.nextInt();
				scan.close();
			} catch (Exception e) {}
		}
		
		if (DOCUMENTS.exists()) {
			
			// Reads all the courses in root directory
			
			String courseFiles[] = DOCUMENTS.list();
			courses = new ArrayList<Course>();
			
			for (int file = 0; file < courseFiles.length; file++) {
				courses.add(new Course(DOCUMENTS, courseFiles[file]));
			}			
		} 
		else {
			DOCUMENTS.mkdir();
		}
		
	}
	
	private boolean createNewUser(char[] username, char[] password) {
		
		// The username can't be taken and the password length must be over 6 characters
		// to be able to create a new user

		if (userExists(username) == USER_DOESNT_EXIST && password.length >= 6) {
			addNewUser(username, password);
			return true;
		}
		
		return false;
		
	}
	
	private char[] getUsername(int id) {
		try {
			// Reads the encrypted username of the given ID
			// and decrypts it to extract the text. Returns null if fail
			
			File nameFile = new File(NAME_FILE + String.valueOf(id));
			FileInputStream reader = new FileInputStream(nameFile);
			
			byte[] buffer = new byte[(int) nameFile.length()];
			reader.read(buffer);
			
			byte[] usernameInBytes = crypter.decrypt(buffer);
			char [] username = new char[usernameInBytes.length];
			for (int i = 0; i < username.length; i++) {
				username[i] = (char) usernameInBytes[i];
			}

			return username;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int loginOk(char[] username, char[] password) {
		
		// If the username exists, the ID is used to check if the password is correct
		
		int id = userExists(username);

		if (id != USER_DOESNT_EXIST) {
			
			if (passwordOk(id, password)) {
				return LOGIN_OK;
			} else {
				return WRONG_PASSWORD;
			}
		}
		else {
			return USER_DOESNT_EXIST;
		}
		
	}
	
	private boolean passwordOk(int id, char[] password) {
		// The passwords are stored in seperated files, defined by their ID number
		// Each file is encrypted and needs to be decrypted in order to compare
		
		try {
			FileInputStream reader = new FileInputStream(new File(PASS_FILE + String.valueOf(id)));
			byte[] encryptedPass = reader.readAllBytes();
			reader.close();
			
			if (crypter.match(password, encryptedPass)) {
				return true;
			}
			
		} catch (Exception e) {}
		
		return false;
	}
	
	private int userExists(char[] username) {
		// Searches for the given username and checks if it exists
		
		for (int id = 1; id <= totalUsers; id++) {
			char[] name = getUsername(id);

			if (Arrays.equals(name, username)) {
				return id;
			}
		}
		
		return USER_DOESNT_EXIST;
	}
	
	private void addNewUser(char[] username, char[] password) {
		
		try {
			
			// The user ID is stored in the users file
			// The username and password is encrypted into seperate files
			// with the ID to identify them
			
			PrintWriter writer = new PrintWriter(users);
			writer.println(++totalUsers);
			writer.close();
			
			FileOutputStream byteWriter = new FileOutputStream(new File(NAME_FILE + String.valueOf(totalUsers)));
			byteWriter.write(crypter.encrypt(username));
			byteWriter.close();
			
			byteWriter = new FileOutputStream(new File(PASS_FILE + String.valueOf(totalUsers)));
			byteWriter.write(crypter.encrypt(password));
			byteWriter.close();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
