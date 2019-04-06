package client;

import java.io.Serializable;

public class ClientRoom implements Serializable {

	private static final long serialVersionUID = 1L;

	private int roomNumber;
	private String roomName;
	private String pw;
	private int pCount;
	private int userCount;
	private int size;
	private boolean state;

	public ClientRoom(int roomNumber, String roomName, String pw, int size, int pCount) {
		this.roomNumber = roomNumber;
		this.roomName = roomName;
		this.pw = pw;
		this.pCount = pCount;
		this.size = size;
		this.userCount = 1;
		this.state = false;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public String getRoomName() {
		return roomName;
	}

	public String getPw() {
		return pw;
	}

	public int getpCount() {
		return pCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public int getSize() {
		return size;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
}
