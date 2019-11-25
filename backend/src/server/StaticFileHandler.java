package server;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StaticFileHandler {

	private static final String CSS_PATTERN = "(?<=(css\"href=\"..)).*?(?=\">)";
	private static final String IMG_PATTERN = "(?<=(imgsrc=\"..)).*?(?=\">)";
	private static final String ROOT = "../frontend";
	
	/** To do: Check if more static files are needed! */
	static File[] checkForStaticFiles(File requestedFile) {
		
		File[] files = new File[2];
		StringBuilder file = new StringBuilder();
		
		try {
			Scanner reader = new Scanner(requestedFile);
			while (reader.hasNext()) {
				file.append(reader.next());
			}
			reader.close();
			
			Matcher cssMatch = Pattern.compile(CSS_PATTERN).matcher(file.toString());
			Matcher imgMatch = Pattern.compile(IMG_PATTERN).matcher(file.toString());
			
			if (cssMatch.find()) {
				files[0] = new File(ROOT + cssMatch.group(0));	
			}
			
			if (imgMatch.find()) {
				files[1] = new File(ROOT + imgMatch.group(0));	
			}
			
		}
		// To do: HANDLE THIS
		catch (IOException e) {
			e.printStackTrace();
		}

		return files;
	}
	
}