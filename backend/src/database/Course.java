package database;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.text.html.HTML;

class Course {

	private final File ROOT;
	private HashMap<Integer, File> chapters;
 
	Course(File base, String endPath) {
		
		ROOT = new File(base, endPath);
		chapters = new HashMap();
		String chapterFiles[] = ROOT.list();

		for (int chapter = 0; chapter < chapterFiles.length; chapter++) {

			try {
				int chap = Integer.parseInt(chapterFiles[chapter].split("_")[1]);	
				chapters.put(chap, new File(ROOT, chapterFiles[chapter]));
			} 
			catch (NullPointerException e) {	// Incorrect naming of chapter
				e.printStackTrace();
			}
		}
		
	}
	
	String getName() {
		return ROOT.getName();
	}
	
	void createChapter(int chapter, String chapterName) {
		chapters.put(chapter, new File(ROOT, chapterName));
	}
	
	boolean containsChapter(int chapter, String chapterName) {
		
		for (int chap : chapters.keySet()) {
			if (chapter == chap) {
				return false;
			}
		}
		
		for (File chap : chapters.values()) {
			if (chap.getName().equals(chapterName)) {
				return false;
			}
		}
		
		return true;
	}
	
	boolean addChapter(int chapter, String filePath) {
		
		File chapterFile = new File(filePath);
		return true;
		
	}
	
	boolean removeChapter(int chapter) {

		try {
			File f = chapters.remove(chapter);
			return f != null;
		} catch (Exception e) {
			return false;
		}

	}
	
}
