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
			ServerSocket serverSocket = new ServerSocket(4221);
			serverSocket.setReuseAddress(true);
			// Wait for connection from client.
			Socket clientSocket = serverSocket.accept(); 

			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			String line = reader.readLine();
			String[] HttpRequest = line.split(" ");

			if(HttpRequest[1].equals("/")) {
				output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
				System.out.println("accepted new connection");
			}
			else {
				output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
			}

			serverSocket.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
}
