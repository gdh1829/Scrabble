package server;

import java.io.IOException;
import java.util.ArrayList;

import client.ClientRoom;

public class Room {

	private ClientRoom r;
	private ArrayList<RoomUser> roomUserList;
	private ArrayList<ServerThread> threadList;

	public Room(ClientRoom r) {
		this.r = r;
		roomUserList = new ArrayList<>();
		threadList = new ArrayList<>();
	}

	public ClientRoom getR() {
		return r;
	}

	public ArrayList<String> getUserList() {
		ArrayList<String> userList = new ArrayList<>();

		for (int i = 0; i < roomUserList.size(); i++) {
			userList.add(roomUserList.get(i).getId());
		}

		return userList;
	}

	public boolean isAllReady() {
		for (int i = 0; i < roomUserList.size(); i++) {
			if (roomUserList.get(i).isReady() == false) {
				return false;
			}
		}

		return true;
	}

	public ArrayList<RoomUser> getRoomUserList() {
		return roomUserList;
	}

	public ArrayList<ServerThread> getThreadList() {
		return threadList;
	}

	public void inputUser(ServerThread serverThread, RoomUser user) {
		threadList.add(serverThread);
		roomUserList.add(user);
	}

	public void removeUser(ServerThread serverThread, RoomUser user) {
		threadList.remove(serverThread);
		roomUserList.remove(user);
	}

	public void makeNumDeck() {

	}

	public void broadCasting(Object[] o) {
		for (ServerThread thread : threadList) {
			try {
				thread.output.writeObject(o);
				thread.output.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
