package database;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

class Message {

	static private SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
	
	static String startScreen(String user, Date time) {
		
		String msg = "====================================\n" + 
				    formatter.format(time) + "\n" +
				    user + "$ " ;
				     
		return msg;
	}
	
	
	
}
