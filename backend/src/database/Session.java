package database;

import java.util.*;
import java.util.StringTokenizer;

public class Session implements Runnable {

	private final String user;
	private final Scanner SCANNER = new Scanner(System.in);
	private final String CREATE = "create", DELETE = "delete", RENAME = "rename",
						 STATUS = "server", SERVICE = "service";
	
	private Database database;
	
	Session(Database database, String user) {
		this.user = user;
		this.database = database;
	}
	
	public static void main(String[] args) {
		new Thread(new Session(new Database(), "victor")).start();
	}
	
	private void showDatabase() {
		
	}
	
	@Override
	public void run() {
		
		boolean sessionAlive = true;
		StringTokenizer tokenizer;
		String command = "";
		
		System.out.print(Message.startScreen(user, new Date()));
		
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
	
	private void userDisplay(String msg) {
		System.out.println(user + "$ " + msg);
	}
	
	private void dispUser() {
		System.out.println(user + "$ ");
	}
	
	private void systemMsg(String msg) {
		System.out.println(msg + "\n" + user + "$ ");
	}
	
	
	void create(StringTokenizer tokenizer, Scanner SCANNER) {
		
		final String COURSE = "course", CHAPTER = "chapter";
		String command = "";
		
				
		command = tokenizer.nextToken();
		
		if (command.equals(COURSE)) {
			
			// If the course name is ok, a new course is added to the database
			try {
				String courseName = tokenizer.nextToken();
				if (database.courseNameOk(courseName)) {
					database.createCourse(courseName);
					systemMsg("Course succesfully added");
				} else {
					systemMsg("Course succesfully added");
				}
			} 
			catch (NoSuchElementException e) {
				systemMsg("Incorrect syntax. Type help for info");
			}
			
			
		} 	
		else if (command.equals(CHAPTER)) {
		
			System.out.println("Which course would you like to add a chapter to?");
			showCourses();
			dispUser();
			
			tokenizer = new StringTokenizer(SCANNER.nextLine());
			
			int courseNumber = Integer.parseInt(tokenizer.nextToken());
			Course course = database.getCourse(courseNumber);
			
			userDisplay("Enter chapter number and filepath");
			
			int chapter = Integer.parseInt(tokenizer.nextToken());
			String chapterName = tokenizer.nextToken();
			
			if (database.chapterOk(course, chapter)) {
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
	
	private void showCourses() {
		StringBuilder courses = new StringBuilder();
		int index = 0;
		for (Course course : database.getAllCourses()) {
			courses.append("[" + String.valueOf(index++) + "] " + course.getName() + "\n");
		}
		systemMsg(courses.toString());
	}

	
 	
}
