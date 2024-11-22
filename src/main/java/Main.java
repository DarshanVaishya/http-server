import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	public static void main(String[] args) throws IOException {
		// You can use print statements as follows for debugging, they'll be visible
		// when running tests.
		System.out.println("Logs from your program will appear here!");
		ServerSocket serverSocket = new ServerSocket(4221);

		try {
			// Create a server on port 4221
			// Allow reuse of address to prevent port in use error
			serverSocket.setReuseAddress(true);
			// Wait for server to receive a request

			while (true) {
				Socket clientSocket = serverSocket.accept();
				ConnectionHandler tp = new ConnectionHandler(clientSocket);
				tp.start();
			}

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} finally {
			serverSocket.close();
		}

	}
}
