package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import vo.MyConstants;

public class ClientThread implements Runnable {
	
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private ClientManager cm;
	
	public ClientThread(String address, int port, ClientManager cm) throws Exception {		
		socket = new Socket(address, port);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		this.cm = cm;
	}

	public ObjectInputStream getInput() {
		return input;
	}
	
	public ObjectOutputStream getOutput() {
		return output;
	}
	
	@Override
	public void run() {
		Object[] obj = null;
		String protocol;
		
		while (true) {
			try {
				obj = (Object[]) input.readObject();
				protocol = (String) obj[0];
				
				switch (protocol) {
					case MyConstants.WAITINGROOM:
						cm.waitingRoom(obj);
						break;
						
					case MyConstants.GAMEROOM:
						cm.gameRoom(obj);
						break;
					default:
						break;
				}
				
			} catch (Exception e) {
				return;
			}
		}
	}
}
