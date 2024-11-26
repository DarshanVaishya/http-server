import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class RequestHandler {
	OutputStream output;
	BufferedReader reader;
	String HTTP_200 = "HTTP/1.1 200 OK\r\n";
	String userAgent = "";

	public RequestHandler(OutputStream output, BufferedReader reader) throws IOException {
		this.output = output;
		this.reader = reader;

		// Check if request has requested HTTP Compression
		// Only gzip is accepted for this server
		String line = reader.readLine();
		while (reader.ready()) {
			if (line.startsWith("Accept-Encoding")) {
				for (String encoder : line.split(" ")) {
					if (encoder.toLowerCase().startsWith("gzip")) {
						this.HTTP_200 = "HTTP/1.1 200 OK\r\nContent-Encoding: gzip\r\n";
					}
				}
			}

			if (line.startsWith("User-Agent"))
				userAgent = line.split(" ")[1];

			line = reader.readLine();
		}
	}

	public void handleHome() throws IOException {
		output.write((HTTP_200 + "\r\n").getBytes());
		System.out.println("Accepted connection at /");
	}

	public void handleEcho(String requestTarget) throws IOException {
		String msg = requestTarget.split("/")[2];
		String s = HTTP_200 + "Content-Type: text/plain\r\nContent-Length: " + msg.length() + "\r\n\r\n"
				+ msg;

		output.write(s.getBytes());
		System.out.println("Echoed message " + msg);
	}

	public void handleUserAgent() throws IOException {
		String res = HTTP_200 + "Content-Type: text/plain\r\nContent-Length: " + userAgent.length()
				+ "\r\n\r\n" + userAgent;
		output.write(res.getBytes());
		System.out.println("User agent " + userAgent + " sent successfully.");
	}

	public void handleUnknown() throws IOException {
		output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
		System.out.println("Rejected connection");
	}
}
