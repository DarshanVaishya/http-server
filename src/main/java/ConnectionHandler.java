import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConnectionHandler extends Thread {
	public Socket clientSocket;
	public String directory;

	public ConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public ConnectionHandler(Socket clientSocket, String directory) {
		this.clientSocket = clientSocket;
		this.directory = directory;
	}

	@Override
	public void run() {
		try {
			// Get streams and reader from the new connection
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			// Read request URL
			String line = reader.readLine();
			if (line == null)
				return;
			System.out.println("Request received: " + line);
			String[] HttpRequest = line.split(" ");

			if (HttpRequest[0].equals("POST")) {
				String fileName = HttpRequest[1].substring(7);
				while(reader.ready()) {
					if(reader.readLine().equals("")) break;
				}

				StringBuffer sf = new StringBuffer();
				while(reader.ready()) {
					sf.append((char) reader.read());
				}
				 Files.write(Path.of(directory + fileName), sf.toString().getBytes());
				output.write("HTTP/1.1 201 Created\r\n\r\n".getBytes());

			} else if (HttpRequest[1].equals("/")) {
				output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
				System.out.println("Accepted connection at /");

			} else if (HttpRequest[1].startsWith("/echo/")) {
				String msg = HttpRequest[1].split("/")[2];
				String s = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + msg.length() + "\r\n\r\n"
						+ msg;
				output.write(s.getBytes());
				System.out.println("Accepted connection at /echo");

			} else if (HttpRequest[1].startsWith("/user-agent")) {
				String userAgent = reader.readLine();
				while (!userAgent.startsWith("User-Agent")) {
					userAgent = reader.readLine();
				}

				userAgent = userAgent.split(" ")[1];
				String res = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + userAgent.length()
						+ "\r\n\r\n" + userAgent;
				output.write(res.getBytes());
				System.out.println("Accepted connection at /user-agent");
			}

			else if (HttpRequest[1].startsWith("/files")) {
				// File name starts after /files/
				String fileName = HttpRequest[1].substring(7);
				File file = new File(directory, fileName);
				if (file.exists()) {
					String fileContent = Files.readString(file.toPath());
					String s = "HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\r\nContent-Length: "
							+ fileContent.length() + "\r\n\r\n" + fileContent;
					output.write(s.getBytes());
				} else {
					output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
				}

			} else {
				output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
				System.out.println("Rejected connection");
			}

			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
