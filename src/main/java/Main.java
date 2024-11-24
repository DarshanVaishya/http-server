import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("Logs from your program will appear here!");
		// Create a server on port 4221
		ServerSocket serverSocket = new ServerSocket(4221);
		System.out.println("Arguements: " + Arrays.toString(args));

		try {
			// Allow reuse of address to prevent port in use error
			serverSocket.setReuseAddress(true);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				if(args.length == 0) {
					ConnectionHandler tp = new ConnectionHandler(clientSocket);
					tp.start();
				}
				else {
					ConnectionHandler tp = new ConnectionHandler(clientSocket, args[1]);
					tp.start();
				}

			}
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} finally {
			serverSocket.close();
		}

	}
}
