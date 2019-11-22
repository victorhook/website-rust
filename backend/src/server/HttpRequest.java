package server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

class HttpRequest implements Runnable {

	private Socket socket;
	private final File ROOT = new File("../frontend/"); // Base directory for frontend
	private final File INDEX_HTML = new File(ROOT, "html/index.html"); // Used to serve default requests ("/")

	HttpRequest(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		BufferedReader in;
		BufferedOutputStream dataOut;
		PrintWriter writer;
		StringTokenizer tokenizer;
		String msg, method, requestedFile;

		try {

			// Prepares streams to read and write to the socket
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			dataOut = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
			writer = new PrintWriter(socket.getOutputStream());

			StringBuilder request = new StringBuilder();
			msg = "";

			// Reads the request as long as the line isn't empty
			while ((msg = in.readLine()).length() != 0) {
				request.append(msg + "\n");
			}
			
			System.out.println(request.toString());

			tokenizer = new StringTokenizer(request.toString());

			method = tokenizer.nextToken();
			requestedFile = tokenizer.nextToken();

			switch (method) {

			case "GET":

				// Default page requested
				if (requestedFile.equals("/")) {
					System.out.println("SERVED: Root page");
					serve(writer, dataOut, INDEX_HTML);
				} else {
					if (fileExists(requestedFile)) {
						System.out.println("SERVED: " + requestedFile);
						serve(writer, dataOut, new File(ROOT, requestedFile));
					} else {
						System.out.println("404?");
						return404();
					}
				}

				break;

			default:
				methodNotImplemented(writer);

			}

		}
		// To do: Handle the exception! Log?
		catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("null!");
			e.printStackTrace();
		}
	}

	private void return404() {

	}

	private byte[] getFileData(File responseFile, int fileSize) {

		try {

			byte buffer[] = new byte[fileSize];
			InputStream reader = new FileInputStream(responseFile);
			reader.read(buffer);
			reader.close();

			return buffer;

		}
		// To do: HANDLE this
		catch (Exception e) {
			e.printStackTrace();
		}

		return null; // This is only reached if an error occurs

	}

	/** Checks if the requested file exists */
	private boolean fileExists(String requestedFile) {

		return new File(ROOT, requestedFile).exists();

		/*
		 * String ext = requestedFile.substring(requestedFile.lastIndexOf(".")); File
		 * responseFile;
		 * 
		 * switch (ext) { case "html": responseFile = new File(ROOT, "html/" +
		 * requestedFile); break;
		 * 
		 * default: responseFile = new File(ROOT, "html/" + requestedFile);
		 * 
		 * }
		 * 
		 * return responseFile.exists();
		 */
	}

	/** Finds the file that was requested and returns the data */
	private void serve(PrintWriter writer, BufferedOutputStream dataOut, File responseFile) {

		System.out.println("Trying to serve page: " + responseFile);

		int fileSize = (int) responseFile.length();
		byte[] fileData = getFileData(responseFile, fileSize);

		writer.println("HTTP/1.1 200 OK");
		writer.println("Server: Java Http Server - Ingenjörshjälp");
		writer.println("Date: " + new Date());
		writer.println("Content-type: " + getContentType(responseFile));
		writer.println("Content-length: " + fileSize);
		writer.println();		// Needed to mark end between headers and content
		writer.flush();			// Flushes the stream buffer
		
		try {
			dataOut.write(fileData);
			dataOut.flush();
		}
		// To do: HANDLE THIS
		catch (IOException e) {
			System.out.println("Error is when serving page");
			e.printStackTrace();
		}

	}

	
	private String getContentType(File fileRequested) {
		
		String contentType;
		
		if (fileRequested.getName().endsWith(".htm") || fileRequested.getName().endsWith(".html")) {
			contentType = "text/html";
		} else {
			contentType = "text/plain";
		}
		return contentType;

	}
	

	private void methodNotImplemented(PrintWriter writer) {

	}

}
