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
	private final File FAVICON = new File(ROOT, "images/favicon-96x96.png");
	private final File FILE404 = new File(ROOT, "html/404.php");

	HttpRequest(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		BufferedReader in;
		BufferedOutputStream dataOut;
		PrintWriter writer;
		StringTokenizer tokenizer;
		String msg, method, requestedFileName;

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
			
			tokenizer = new StringTokenizer(request.toString());

			method = tokenizer.nextToken();
			requestedFileName = tokenizer.nextToken();

			switch (method) {

			case "GET":

				// Default page requested
				if (requestedFileName.equals("/")) {
					
					serve(writer, dataOut, INDEX_HTML);
					serve(writer, dataOut, FAVICON);
					
					// Checks if any static files are needed in the served document 
					// If they are, these files are also sent as a response
					serveStaticFiles(writer, dataOut, INDEX_HTML);
					
				} else {
					if (fileExists(requestedFileName)) {
						
						File requestedFile = new File(ROOT, requestedFileName);
						serve(writer, dataOut, requestedFile);
						
						serveStaticFiles(writer, dataOut, requestedFile);
						
						
					} else {
						return404(writer, dataOut);
					}
				}

				break;

			default:
				methodNotImplemented(writer);

			}
			
			try {
				// Tries to close socket and all streams
				socket.close();
				in.close();
				dataOut.close();;
				writer.close();
			}
			catch (Exception e) {
				System.out.println("Error closing streams!");
				e.printStackTrace();
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
	
	/** If there's any static files needed in the response, those are sent through this method */
	private void serveStaticFiles(PrintWriter writer, BufferedOutputStream dataOut, File requestedFile) {
		
		File[] staticFiles = StaticFileHandler.checkForStaticFiles(requestedFile);
		int i = 0;
		
		while (i < staticFiles.length && staticFiles[i] != null) {
			System.out.println(staticFiles[i]);
			serve(writer, dataOut, staticFiles[i++]);
		}
		
	}

	private void return404(PrintWriter writer, BufferedOutputStream dataOut) {
		
		System.out.println("Trying to serve page: " + FILE404);

		int fileSize = (int) FILE404.length();
		byte[] fileData = getFileData(FILE404, fileSize);

		writer.println("HTTP/1.1 404 Not Found");
		writer.println("Server: Java Http Server - Ingenjörshjälp");
		writer.println("Date: " + new Date());
		writer.println("Content-type: " + getContentType(FILE404));
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

	/** Checks the extension of the requested file to match the content-type in the response */
	private String getContentType(File fileRequested) {
		
		String extension = fileRequested.getName().substring(fileRequested.getName().lastIndexOf('.'));
		String contentType;
		
		switch (extension) {
		case ".htm":
		case ".html":
		case ".php":
			contentType = "text/html";
			break;
		case ".css":
			contentType = "text/css";
			break;
		case ".js":
			contentType = "text/javascript";
			break;
		case ".png":
			contentType = "image/png";
			break;
		case ".jpeg":
			contentType = "image/jpeg";
			break;
		default:
			contentType = "text/plain";
		}

		return contentType;

	}
	

	private void methodNotImplemented(PrintWriter writer) {

	}

}
