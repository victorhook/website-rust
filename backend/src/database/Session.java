package database;

import java.util.*;
import java.util.StringTokenizer;

public class Session implements Runnable {

	private final int ID;
	private final Scanner SCANNER = new Scanner(System.in);
	private boolean sessionAlive;
	private final String CREATE = "create", DELETE = "delete", RENAME = "rename",
						 STATUS = "server", SERVICE = "service";
	
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
				
				switch (command) {
				case CREATE:
					create(tokenizer, SCANNER);
					break;
				case DELETE:
					delete(tokenizer);
					break;
				case RENAME:
					rename(tokenizer);
					break;
				case STATUS:
					status(tokenizer);
					break;
				case SERVICE:
					service(tokenizer);
					break;
				}

			}

		}
		
	}
	
	void create(StringTokenizer tokenizer, Scanner SCANNER) {
		
		final String COURSE = "course", CHAPTER = "chapter";
		String command = "";
		
				
		command = tokenizer.nextToken();
		
		if (command.equals(COURSE)) {
			
			// If the course name is ok, a new course is added to the database
			String courseName = tokenizer.nextToken();
			if (database.courseNameOk(courseName)) {
				database.createCourse(courseName);
			}
			
		} 	
		else if (command.equals(CHAPTER)) {
		
			showChapters();
			
			tokenizer = new StringTokenizer(SCANNER.nextLine());
			
			int courseNumber = Integer.parseInt(tokenizer.nextToken());
			Course course = database.getCourse(courseNumber);
			
			int chapter = Integer.parseInt(tokenizer.nextToken());
			String chapterName = tokenizer.nextToken();
			
			if (database.chapterOk(course, chapter, chapterName)) {
				database.createChapter(course, chapter, chapterName);
			} else {
				errorMessage("Chapter not okay");
			}
			
		}

		
		
		
	}

	void delete(StringTokenizer tokenizer) {
			
		}
	
	void rename(StringTokenizer tokenizer) {
		
	}
	
	void status(StringTokenizer tokenizer) {
		
	}
	
	void service(StringTokenizer tokenizer) {
		
	}
	
	void errorMessage(String msg) {
		
	}
	
	void showChapters() {
		
	}

	
 	
}
