package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server Main
 */
public class ServerMain {

	int port; // server port
	ServerSocket serverSocket; // server socket

	public static void main(String[] args) {
		new ServerMain(9633).start();
	}

	public ServerMain(int port) {
		this.port = port;
	}

	public void start() {
		Socket socket = null;
		ServerThread thread = null;

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server Starts.");
		} catch (IOException e) {
			System.out.println("Failed to start server.");
		}

		boolean flag = true;
		while (flag) {
			try {
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress().getHostAddress() + " accessed.");

				thread = new ServerThread(socket);
				new Thread(thread).start();
			} catch (IOException e) {
				flag = false;
				System.out.println(e.getMessage());
			}
		}
	}
}
