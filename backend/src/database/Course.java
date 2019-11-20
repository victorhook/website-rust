package database;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class Course {

	private final File ROOT;
	private ArrayList<File> chapters;
 
	Course(File base, String endPath) {
		
		// Creates a new directory if the course is new
		ROOT = new File(base, endPath);
		if (!ROOT.exists()) {
			ROOT.mkdir();
		}
		
		// Adds all chapters that the course contains
		chapters = new ArrayList<File>();
		String chapterFiles[] = ROOT.list();

		for (String chapter : chapterFiles) {
			chapters.add(new File(chapter));
		}
		
	}
	
	String getName() {
		return ROOT.getName();
	}

	/** Makes a copy of the given chapter and saves it in the correct directory and 
	 *  The old file is kept untouched. Returns false if something fails  */
	boolean createChapter(int chapter, String filePath) {
		
		File chapterFile = new File(filePath);
		char[] stringBuffer = new char[filePath.length()];
		try  {
			FileReader reader = new FileReader(chapterFile);
			reader.read(stringBuffer);
			reader.close();
		} 
		catch (IOException e) {
			return false;
		}
		
		try {
			// Saves the file in the new, correct directory, with the correct naming convention: Chapter_X
			File newChapter = new File(ROOT, "Chapter_" + chapter);
			PrintWriter writer = new PrintWriter(newChapter);
			writer.write(stringBuffer);
			writer.close();
			chapters.add(newChapter);
		}
		catch (IOException e) {
			return false;
		}
		
		return true;
	}

	/** Checks if the given chapter exists in the course */
	boolean containsChapter(int chapterNumber) {
		
		for (File chapter : chapters) {
			int chap = Integer.valueOf(chapter.getName().split("_")[1]);
			if (chap == chapterNumber) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/** Tries to remove a given chapter and returns false if it doesn't exists */
	boolean removeChapter(int chapterNumber) {
		
		for (File chapter : chapters) {
			int chap = Integer.valueOf(chapter.getName().split("_")[1]);
			if (chap == chapterNumber) {
				chapters.remove(chapter);
				return true;
			}
		}
		return false;
	}
	
}
