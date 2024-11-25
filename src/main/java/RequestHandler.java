import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class RequestHandler extends Thread {
	OutputStream output;
	BufferedReader reader;

	public RequestHandler(OutputStream output, BufferedReader reader) {
		this.output = output;
		this.reader = reader;
	}

	public void handleHome() throws IOException {
		output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
		System.out.println("Accepted connection at /");
	}

	public void handleEcho(String requestedResource) throws IOException {
		String msg = requestedResource.split("/")[2];
		String s = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + msg.length() + "\r\n\r\n"
				+ msg;

		output.write(s.getBytes());
		System.out.println("Echoed message " + msg);
	}

	public void handleUserAgent() throws IOException {
		String line = reader.readLine();
		while (!line.startsWith("User-Agent")) {
			line = reader.readLine();
		}

		String userAgent = line.split(" ")[1];
		String res = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + userAgent.length()
				+ "\r\n\r\n" + userAgent;
		output.write(res.getBytes());
		System.out.println("User agent " + userAgent + " sent successfully.");
	}

	public void handleUnknown() throws IOException {
		output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
		System.out.println("Rejected connection");
	}
}
