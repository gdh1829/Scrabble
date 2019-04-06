package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import vo.MyConstants;
import vo.MyGameData;

public class ClientManager {

	private ClientThread thread;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	private WaitingRoom wr;
	private GameRoom gr;

	public ClientManager(String address, int port) throws Exception {
		thread = new ClientThread(address, port, this);
		output = thread.getOutput();
		input = thread.getInput();
	}

	public void start() {
		new Thread(thread).start();
	}

	public void setWr(WaitingRoom wr) {
		this.wr = wr;
	}

	public void setGr(GameRoom gr) {
		this.gr = gr;
	}

	public void sendRequest(Object[] obj) {
		try {
			output.writeObject(obj);
			output.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object communicate(Object[] obj) {
		Object o = null;

		try {
			output.writeObject(obj);
			output.reset();
			o = input.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return o;
	}

	/******************************** Login ********************************/
	public boolean addClient(String id, char[] pw) {
		Object[] obj = { MyConstants.LOGIN, MyConstants.COMMAND_ADDCLIENT, id, pw };

		return (boolean) communicate(obj);
	}

	public int checkClient(String id, char[] pw) {
		Object[] obj = { MyConstants.LOGIN, MyConstants.COMMAND_CHECKCLIENT, id, pw };

		return (int) communicate(obj);
	}

	/******************************** Waiting room ********************************/
	public void waitingRoom(Object[] obj) {
		String cmd = (String) obj[1];

		switch (cmd) {
		case MyConstants.COMMAND_SENDMESSAGE:
			wr.showMessage((String) obj[2], (String) obj[3]);
			break;

		case MyConstants.COMMAND_FIRSTCONNECTION:
			wr.showMessage((String) obj[2], (String) obj[3]);
			wr.refreshUserList((ArrayList<String>) obj[4]);
			wr.refreshRoomList((ArrayList<ClientRoom>) obj[5]);
			break;

		case MyConstants.COMMAND_CREATEROOM:
			wr.refreshUserList((ArrayList<String>) obj[4]);
			wr.refreshRoomList((ArrayList<ClientRoom>) obj[5]);
			break;

		case MyConstants.COMMAND_ENTERROOM:
			wr.refreshUserList((ArrayList<String>) obj[4]);
			wr.refreshRoomList((ArrayList<ClientRoom>) obj[5]);
			break;

		case "getRoomPW":
			wr.pwChecker((ClientRoom) obj[3]);
			break;

		case MyConstants.COMMAND_DISCONNECTION:
			wr.showMessage((String) obj[2], (String) obj[3]);
			wr.refreshUserList((ArrayList<String>) obj[4]);
			break;
		default:
			break;
		}
	}

	public void firstConnection(String id) {
		Object[] obj = { "WaitingRoom", "firstConnection", id, " loged in." };
		sendRequest(obj);
	}

	public void sendMessage(String id, String message) {
		Object[] obj = { "WaitingRoom", "sendMessage", id, message };
		sendRequest(obj);
	}

	public void createRoom(String id, ClientRoom r) {
		Object[] obj = { "WaitingRoom", "createRoom", id, r };
		sendRequest(obj);
	}

	public void enterRoom(String id, int roomNumber) {
		Object[] obj = { "WaitingRoom", "enterRoom", id, roomNumber };
		sendRequest(obj);
	}

	public void getRoomPW(int roomNumber) {
		Object[] obj = { "WaitingRoom", "getRoomPW", roomNumber, "" };
		sendRequest(obj);
	}

	public void disconnection(String id) {
		Object[] obj = { "WaitingRoom", "disconnection", id, " exits." };
		sendRequest(obj);
	}

	/******************************** Game room ********************************/
	public void gameRoom(Object[] obj) {
		String cmd = (String) obj[1];

		switch (cmd) {
		case MyConstants.COMMAND_ENTER:
			gr.refreshUserList((ArrayList<String>) obj[4]);
			gr.dynamicLabelMake();
			gr.showMessageEnteredRoom();
			break;

		case MyConstants.COMMAND_READY:
			gr.gameStart((boolean) (obj[4]), (String) obj[3]/* , (int[]) obj[5] */); //
			// gr.selected((String) obj[3]);
			break;

		case MyConstants.COMMAND_START:
			wr.refreshRoomList((ArrayList<ClientRoom>) obj[3]);
			break;

		case MyConstants.COMMAND_SUBMIT:
			MyGameData receivedData = (MyGameData) obj[4];
			gr.submit(receivedData.getCurrentUserId(), (String) obj[3], receivedData);
			break;

		case MyConstants.COMMAND_SHUFFLE://
			String currentId = (String) obj[3];//
			int minusScore = (int) obj[4];//
			gr.shuffle(currentId, minusScore);//
			break;

		case MyConstants.COMMAND_SENDMESSAGE:
			gr.showMessage((String) obj[3], (String) obj[4]);
			break;

		case MyConstants.COMMAND_END:
			gr.gameLost((String) obj[3]);
			break;

		case MyConstants.COMMAND_EXIT:
			wr.refreshUserList((ArrayList<String>) obj[2]);
			wr.refreshRoomList((ArrayList<ClientRoom>) obj[3]);
			gr.refreshUserList((ArrayList<String>) obj[4]);
			gr.dynamicLabelMake();
			break;

		default:
			break;
		}
	}

	public void enter(int roomNumber, String id) {
		Object[] obj = { "GameRoom", "enter", roomNumber, id, "" };
		sendRequest(obj);
	}

	public void sendMessage(int roomNumber, String id, String message) {
		Object[] obj = { "GameRoom", "sendMessage", roomNumber, id, message };
		sendRequest(obj);
	}

	public void ready(int roomNumber, String id, boolean isReady) {
		Object[] obj = { "GameRoom", "ready", roomNumber, id, isReady, "" };
		sendRequest(obj);
	}

	public void gameStart(int roomNumber) {
		Object[] obj = { "GameRoom", "start", roomNumber, "" };
		sendRequest(obj);
	}

	public void submit(int roomNumber, String id, MyGameData gameData) {
		Object[] obj = { "GameRoom", "submit", roomNumber, id, gameData };
		sendRequest(obj);
	}

	public void shuffle(int roomNumber, String id, int shuffleMinusScore) { /////////////
		Object[] obj = { MyConstants.GAMEROOM, MyConstants.COMMAND_SHUFFLE, roomNumber, id, shuffleMinusScore };
		sendRequest(obj);
	}

	public void gameEnd(int roomNumber, String id) {
		Object[] obj = { MyConstants.GAMEROOM, MyConstants.COMMAND_END, roomNumber, id };
		sendRequest(obj);
	}

	public void exit(int roomNumber, String id) {
		Object[] obj = { "GameRoom", "exit", roomNumber, id };
		sendRequest(obj);
	}

	public void close() throws Exception {
		input.close();
		output.close();
	}
}
