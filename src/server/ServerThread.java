package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import client.ClientRoom;
import vo.ColWord;
import vo.MyConstants;
import vo.MyGameData;
import vo.RowWord;
import vo.Word;

public class ServerThread implements Runnable {

	public static ArrayList<ServerThread> list = new ArrayList<ServerThread>(); // Accessing user list
	public static ArrayList<String> usernames = new ArrayList<String>(); // Accessing user id list
	public static ArrayList<Room> roomList = new ArrayList<Room>(); // Game room list

	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	String username;
	String addr;
	ServerManager sm;

	public ServerThread(Socket socket) {
		try {
			this.socket = socket;
			// Creates IO stream to connect with client side
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			addr = socket.getInetAddress().getHostAddress();
			list.add(this);
			sm = new ServerManager();
		} catch (IOException e) {
			System.out.println("Failed to connect with " + addr);
		}
	}

	// Waits for connection with client side
	@Override
	public void run() {
		Object[] obj = null;
		String type;

		while (socket.isConnected()) {
			try {
				obj = (Object[]) input.readObject();
				type = (String) obj[0];

				switch (type) {
				// New commer
				case "Login":
					login(obj);
					break;

				case "WaitingRoom":
					waitingRoom(obj);
					break;

				case "GameRoom":
					gameRoom(obj);
					break;
				}

			} catch (Exception e) {
				return;
			}
		}
	}

	public void login(Object[] obj) {
		String protocol = (String) obj[1];
		String id, pw;

		try {
			switch (protocol) {
			case "addClient":
				id = (String) obj[2];
				pw = new String((char[]) obj[3]);
				output.writeObject(sm.addClient(id, pw));
				break;

			case "checkClient":
				id = (String) obj[2];
				pw = new String((char[]) obj[3]);

				if (findConnectedUser(id)) {
					output.writeObject(2);
				} else {
					output.writeObject(sm.checkClient(id, pw));
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Waitng room
	 */
	public void waitingRoom(Object[] obj) {
		String protocol = (String) obj[1];
		ArrayList<ClientRoom> rList = refreshRoomUserList();
		Object[] server_obj = { obj[0], obj[1], obj[2], obj[3], usernames, rList };
		Room r = null;

		switch (protocol) {
		// initial connection
		case "firstConnection":
			usernames.add((String) obj[2]);
			broadCasting(server_obj);
			break;
		// Transfer chat messages
		case "sendMessage":
			broadCasting(obj);
			break;
		case "createRoom":
			r = new Room((ClientRoom) obj[3]);
			r.inputUser(this, new RoomUser((String) obj[2]));
			usernames.remove((String) obj[2]);
			roomList.add(r);
			server_obj[5] = refreshRoomUserList();
			broadCasting(server_obj);
			break;
		case "enterRoom":
			usernames.remove((String) obj[2]);
			// broadCasting(server_obj);
			// finds a game room
			r = findRoom((int) obj[3]);
			// Inputs serverThread
			r.inputUser(this, new RoomUser((String) obj[2]));
			r.getR().setUserCount(refreshRoomUserList().size());
			server_obj[5] = refreshRoomUserList();
			broadCasting(server_obj);
			break;
		case "getRoomPW":
			r = findRoom((int) obj[2]);
			obj[3] = r.getR();
			try {
				output.writeObject(obj);
				output.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "disconnection":
			usernames.remove((String) obj[2]);
			list.remove(this);
			broadCasting(server_obj);
			close();
			break;
		default:
			break;
		}
	}

	public ArrayList<ClientRoom> refreshRoomUserList() {
		ArrayList<ClientRoom> rList = new ArrayList<>();
		for (int i = 0; i < roomList.size(); i++) {
			rList.add(roomList.get(i).getR());
			roomList.get(i).getR().setUserCount(roomList.get(i).getRoomUserList().size());
		}

		return rList;
	}

	public boolean findConnectedUser(String id) {
		boolean isConnected = false;

		for (int i = 0; i < usernames.size(); i++) {
			if (id.equals(usernames.get(i))) {
				isConnected = true;
			}
		}

		for (int i = 0; i < roomList.size(); i++) {
			for (int j = 0; j < roomList.get(i).getRoomUserList().size(); j++) {
				if (id.equals(roomList.get(i).getRoomUserList().get(j))) {
					isConnected = true;
				}
			}
		}

		return isConnected;
	}

	public RoomUser findUserIntheRoom(Room r, String id) {
		for (int i = 0; i < r.getRoomUserList().size(); i++) {
			if (r.getRoomUserList().get(i).getId().equals(id)) {
				return r.getRoomUserList().get(i);
			}
		}

		return null;
	}

	public Room findRoom(int roomNumber) {
		Room r = null;

		for (int i = 0; i < roomList.size(); i++) {
			if (roomList.get(i).getR().getRoomNumber() == roomNumber) {
				r = roomList.get(i);
			}
		}

		return r;
	}

	/**
	 * Game room
	 */
	public void gameRoom(Object[] obj) {
		String protocol = (String) obj[1];
		int roomNo = (int) obj[2];
		Room r = findRoom(roomNo);
		String id = (String) obj[3];
		RoomUser user = null;

		switch (protocol) {
		case "enter":
			obj[4] = r.getUserList();
			r.broadCasting(obj);
			break;
		case "ready":
			user = findUserIntheRoom(r, id);
			user.setReady((boolean) obj[4]);
			obj[4] = r.isAllReady();
			if (r.isAllReady()) {
				// Starter Id
				obj[3] = r.getRoomUserList().get(0).getId();
				// int[][] deckNumSet = new int[4][9];
				// for (int i = 0; i < r.getRoomUserList().size(); i++) {
				// for (int j = 0; j < 9; j++) {
				// deckNumSet[i][j] =
				// }
				// }
			}
			r.broadCasting(obj);
			break;
		case "start":
			r.getR().setState(true);
			obj[3] = refreshRoomUserList();
			broadCasting(obj);
			break;
		case "submit":
			// Checks the next turn
			int nextTurn = r.getUserList().indexOf(id) + 1;

			if (r.getRoomUserList().size() == nextTurn) {
				user = r.getRoomUserList().get(0);
			} else {
				user = r.getRoomUserList().get(nextTurn);
			}

			String nextUserId = user.getId();
			System.out.println(user.getId());

			MyGameData gameData = (MyGameData) obj[4];
			Word word = gameData.getWord();
			ArrayList<RowWord> rowWords = word.getRowwords();
			ArrayList<ColWord> colWords = word.getColwords();

			ArrayList<int[]> selectedBtnXYListbyMousePressed = gameData.getSelectedBtnXYListByMousePressed();
			HashMap<int[], String> alphabetListbyMousePressed = gameData.getAlphabetListbyMousePressed();

			System.out.println("Inspects rows");
			ArrayList<int[]> row_resultXY = sm.row_wordChecker(rowWords, selectedBtnXYListbyMousePressed,
					alphabetListbyMousePressed);
			System.out.println("Result of row inspection");
			if (row_resultXY.isEmpty()) {
				System.out.println("row Nothing");
			} else {
				for (int[] a : row_resultXY) {
					System.out.println(a[0] + " " + a[1]);
				}
			}

			System.out.println("Inspects columns");
			ArrayList<int[]> col_resultXY = sm.col_wordChecker(colWords, selectedBtnXYListbyMousePressed,
					alphabetListbyMousePressed);
			System.out.println("Result of column inspection");
			if (col_resultXY.isEmpty()) {
				System.out.println("col Nothing");
			} else {
				for (int[] a : col_resultXY) {
					System.out.println(a[0] + " " + a[1]);
				}
			}

			int plusScore = 0;
			if (sm.answerCollection.isEmpty()) {
				System.out.println("Nothing correct");
			} else {
				System.out.println("Right Answer");
				for (String str : sm.answerCollection) {
					System.out.println(str);
					int temp = 10 * str.length();
					plusScore += temp;
				}
			}

			MyGameData returnData = new MyGameData(row_resultXY, col_resultXY, (String) obj[3], plusScore,
					sm.getAnswerCollection(), sm.getRow_stringCollectionMap(), sm.getCol_stringCollectionMap());

			r.broadCasting(
					new Object[] { MyConstants.GAMEROOM, MyConstants.COMMAND_SUBMIT, roomNo, nextUserId, returnData });
			sm.initAnswerCollection();
			sm.initRow_stringCollectionMap();
			sm.initCol_stringCollectionMap();
			break;

		case MyConstants.COMMAND_SHUFFLE:
			id = (String) obj[3];
			int minusScore = (int) obj[4];
			r.broadCasting(new Object[] { MyConstants.GAMEROOM, MyConstants.COMMAND_SHUFFLE, roomNo, id, minusScore });
			break;

		case "sendMessage":
			r.broadCasting(obj);
			break;

		case "End":
			r.broadCasting(obj);
			break;

		case "exit":
			id = (String) obj[3];

			// Finds users
			for (int i = 0; i < r.getRoomUserList().size(); i++) {
				if (r.getRoomUserList().get(i).getId().equals(id)) {
					user = r.getRoomUserList().get(i);
				}
			}
			// remove the users who have entered game rooms
			r.removeUser(this, user);

			System.out.println(r.getRoomUserList().size());

			// When none of users is in the game room, removes the room
			if (r.getRoomUserList().size() == 0) {
				roomList.remove(r);
			}

			usernames.add((String) obj[3]);

			broadCasting(new Object[] { "GameRoom", "exit", usernames, refreshRoomUserList(), r.getUserList() });
			break;

		default:
			break;
		}
	}

	// 전체 접속자에게 전송
	public void broadCasting(Object[] o) {
		System.out.println("Broadcasting : Number of Clients : " + list.size());
		System.out.println("Number of Users : " + usernames.size());
		System.out.println("Number of Game Rooms : " + roomList.size());
		for (ServerThread thread : list) {
			try {
				thread.output.writeObject(o);
				thread.output.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Disconnects connection
	public void close() {
		try {
			output.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
