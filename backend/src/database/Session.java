package database;

import java.util.*;
import java.util.StringTokenizer;

public class Session implements Runnable {

	private final String user;
	private final Scanner SCANNER = new Scanner(System.in);
	private final String CREATE = "create", DELETE = "delete", RENAME = "rename",
						 STATUS = "server", SERVICE = "service";
	private final String COURSE = "course", CHAPTER = "chapter";
	
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
			
			prompt();
		
			tokenizer = new StringTokenizer(SCANNER.nextLine());
			
			while (tokenizer.hasMoreTokens()) {
				
				command = tokenizer.nextToken();
				
				// If the command is quit or exit, the sessions is over
				sessionAlive = !(command.equals("quit") || command.equals("exit"));
				
				switch (command) {
				case CREATE:
					create(tokenizer, SCANNER);
					break;
				case DELETE:
					delete(tokenizer, SCANNER);
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
	
	void prompt() {
		System.out.print(user + "$ ");
	}
	
	private void systemMsg(String msg) {
		System.out.println(msg);
		prompt();
	}
	
	/** Users can delete courses or chapters 
	 * @param tokenizer used to parse commands
	 * @param SCANNER allow input stream to read commands from user
	 * */
	private void delete(StringTokenizer tokenizer, Scanner SCANNER) {
		
		String command = "";
		try {
			command = tokenizer.nextToken();
			
			if (command.equals(COURSE)) {
				
				System.out.println("Which course would you like to delete? Enter the course name");
				showCourses();
				
				try {
					String courseName = SCANNER.next();
					
					if (database.courseExists(courseName)) {
						
						systemMsg("Are you sure you want to delete the course? All the chapters will be removed\n" + 
								  "y/n");
						
						command = SCANNER.next();
						
						if (command.toLowerCase().equals("y")) {
							if (database.deleteCourse(courseName)) {
								systemMsg("Course " + courseName + " removed");		
							} else {
								systemMsg("Failed to delete course");
							}
						}
						return;
					}
					systemMsg("Failed to delete course " + courseName);
				} 
				catch (NoSuchElementException e) {
					e.printStackTrace();
				}
				
			}
			
			else if (command.equals(CHAPTER)) {
				
				System.out.println("From which course would you like to delete? Enter the course name");
				showCourses();
				
				try {
					String courseName = SCANNER.next();
					
					if (database.courseExists(courseName)) {
						
						System.out.println("Which chapter do you want to delete?");
						showChapters(courseName);
						
						String chapter = SCANNER.next();
						/*
						if (database.deleteChapter(chapter, courseName)) {
							systemMsg("Chapter " + chapter + " deleted from " + courseName);
						}
						*/
						if (command.toLowerCase().equals("y")) {
							if (database.deleteCourse(courseName)) {
								systemMsg("Course " + courseName + " removed");		
							} else {
								systemMsg("Failed to delete course");
							}
						}
						return;
					}
					systemMsg("Failed to delete course " + courseName);
				} 
				catch (NoSuchElementException e) {
					e.printStackTrace();
				}
				
			}
		
			
		} 
		// Gets thrown if there is incorrect syntax in the command
		catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/** Users can create new courses or chapters. These are automatically placed in the correct 
	 * locations in the database, and named according to my naming convention
	 * @param tokenizer used to parse commands
	 * @param SCANNER allow input stream to read commands from user 
	 */
	private void create(StringTokenizer tokenizer, Scanner SCANNER) {
		
		String command = "";
				
		command = tokenizer.nextToken();
		
		if (command.equals(COURSE)) {
			
			// If the course name is ok, a new course is added to the database
			try {
				String courseName = tokenizer.nextToken();
				if (database.courseNameOk(courseName)) {
					database.createCourse(courseName);
					systemMsg("Course " + courseName + " succesfully added");
				} 
			} 
			catch (NoSuchElementException e) {
				systemMsg("~ Syntax is: create course \"COURSENAME\"");
			}
			
			
		} 	
		else if (command.equals(CHAPTER)) {
		
			try {
				
				// Tries to add a new chapter to the selected course
				
				System.out.println("Which course would you like to add a chapter to?");
				showCourses();
				
				int courseNumber = Integer.valueOf(SCANNER.nextLine());
				Course course = database.getCourse(courseNumber);
				
				
				systemMsg("Enter chapter number and filepath, seperated by white space");
				
				String input = SCANNER.nextLine();
				
				int chapter = Integer.parseInt(input.split(" ")[0]);
				String chapterName = input.split(" ")[1];
				
				if (database.chapterOk(course, chapter, chapterName)) {
					database.createChapter(course, chapter, chapterName);
					System.out.println("Chapter " + chapter + " added to " + course.getName());
				} else {
					// Something went wrong with adding the chapter
					System.out.println("~ Either the chapter already exists or the given filepath\n" + 
							           "  is incorrect. Type help for help");
				}
			}
			// Gets thrown if incorrect syntax is given, like an int instead of a string
			catch (Exception e) {
				
			}
			
		}
		
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
	
	/** Retrives all the chapters from the given course and displays them */
	private void showChapters(String courseName) {
		StringBuilder message = new StringBuilder();
		String[] chapters = database.getChapters(courseName);
		
		if (chapters != null) {
			for (int i = 0; i < chapters.length; i++) {
				message.append("[" + String.valueOf(i) + "] " + chapters[i++] + "\n");
			}
			systemMsg(message.toString());
		} else {
			systemMsg("Failed to find course");
		}
		
	}
	
	/** Displays all courses */
	private void showCourses() {
		StringBuilder courses = new StringBuilder();
		int index = 0;
		for (Course course : database.getAllCourses()) {
			courses.append("[" + String.valueOf(index++) + "] " + course.getName() + "\n");
		}
		systemMsg(courses.toString());
	}

	
 	
}
