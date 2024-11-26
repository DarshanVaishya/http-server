import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

class ConnectionHandler extends Thread {
	public Socket clientSocket;
	public String directory;

	public ConnectionHandler(Socket clientSocket,  String directory) {
		this.clientSocket = clientSocket;
		this.directory = directory;
	}
	
	@Override
	public void run() {
			try {
				InputStream input = clientSocket.getInputStream();
				OutputStream output = clientSocket.getOutputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));

				// Read request URL
				String line = reader.readLine();
				if (line == null)
					return;
				System.out.println();
				System.out.println("Request received: " + line);
				String[] requestLine = line.split(" ");
				String requestMethod = requestLine[0];
				String requestTarget = requestLine[1];

				if(requestTarget.startsWith("/files")) {
					FileHandler handler = new FileHandler(directory, requestTarget);
					if(requestMethod.equals("POST")) {
						handler.HandlePost(directory, reader, output);
					} else {
						handler.HandleGet(output);
					}

				} else {
					RequestHandler handler = new RequestHandler(output, reader);
					if(requestTarget.equals("/")) {
							handler.handleHome();
					} else if(requestTarget.startsWith("/echo")) {
						handler.handleEcho(requestTarget);
					} else if(requestTarget.startsWith("/user-agent")) {
						handler.handleUserAgent();
					} else {
						handler.handleUnknown();
					}
				}

				input.close();
				output.close();
				reader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
