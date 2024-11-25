import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class FileHandler {
	String directory;
	String requestTarget;

	public FileHandler(String directory, String requestTarget) {
		this.directory = directory;
		this.requestTarget = requestTarget;
	}

	public void HandleGet(OutputStream output) throws IOException {
		// File name starts after /files/
		String fileName = requestTarget.substring(7);
		File file = new File(directory, fileName);
		if (file.exists()) {
			String fileContent = Files.readString(file.toPath());
			String s = "HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\r\nContent-Length: "
					+ fileContent.length() + "\r\n\r\n" + fileContent;

			output.write(s.getBytes());
			System.out.println("File " + directory + fileName + " read.");
			System.out.println("File content: " + fileContent);
		} else {
			output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
		}
	}

	public void HandlePost(String directory, String requestTarget, BufferedReader reader, OutputStream output)
			throws IOException {
		String fileName = requestTarget.substring(7);

		// Once there is an empty line then the file content starts
		while (reader.ready()) {
			if (reader.readLine().equals(""))
				break;
		}

		// Start reading the file content
		StringBuffer sf = new StringBuffer();
		while (reader.ready()) {
			sf.append((char) reader.read());
		}

		// Write content to the file from the request
		Files.write(Path.of(directory + fileName), sf.toString().getBytes());

		output.write("HTTP/1.1 201 Created\r\n\r\n".getBytes());
		System.out.println("File " + directory + fileName + " created.");
		System.out.println("File content: " + sf.toString());
	}
}
