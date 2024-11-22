import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ConnectionHandler extends Thread {
	public Socket clientSocket;

	public ConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			// Get streams and reader from the new connection
			InputStream input;
			input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			// Read request URL
			String line = reader.readLine();
			if(line == null)
				return;
			System.out.println("Request received: " + line);
			String[] HttpRequest = line.split(" ");
			System.out.println(Arrays.toString(HttpRequest));

			if (HttpRequest[1].equals("/")) {
				output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
				System.out.println("Accepted connection at /");

			} else if (HttpRequest[1].startsWith("/echo/")) {
				String msg = HttpRequest[1].split("/")[2];
				String s = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + msg.length()
						+ "\r\n\r\n" + msg;
				output.write(s.getBytes());
				System.out.println("Accepted connection at /echo");

			} else if (HttpRequest[1].startsWith("/user-agent")) {
				String userAgent = reader.readLine();
				while (!userAgent.startsWith("User-Agent")){
					userAgent = reader.readLine();
				}

				userAgent = userAgent.split(" ")[1];
				String res = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + userAgent.length()
						+ "\r\n\r\n" + userAgent;
				output.write(res.getBytes());
				System.out.println("Accepted connection at /user-agent");

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
