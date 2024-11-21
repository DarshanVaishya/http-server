import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	public static void main(String[] args) {
		// You can use print statements as follows for debugging, they'll be visible when running tests.
		System.out.println("Logs from your program will appear here!");

		try {
			// Create a server on port 4221
			ServerSocket serverSocket = new ServerSocket(4221);
			// Allow reuse of address to prevent port in use error
			serverSocket.setReuseAddress(true);
			// Wait for server to receive a request
			Socket clientSocket = serverSocket.accept(); 

			// Get streams and reader from the new connection
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			// Read request URL
			String line = reader.readLine();
			String[] HttpRequest = line.split(" ");

			// If requested URL is the home then accept connection
			if(HttpRequest[1].equals("/")) {
				output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
				System.out.println("Accepted new connection");
			}
			// Otherwise reject the connection
			else {
				output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
				System.out.println("Rejected new connection");
			}

			input.close();
			output.close();
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
}
