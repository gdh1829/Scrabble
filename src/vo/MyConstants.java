package vo;

import java.io.Serializable;

public class MyConstants implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/******************* 1�� Ŀ�ǵ� ********************************/
	public static final String LOGIN = "Login";
	public static final String WAITINGROOM = "WaitingRoom";
	public static final String GAMEROOM = "GameRoom";

	public static final String GRID_STATUS_NORMAL = "Normal";
	public static final String GRID_STATUS_NEW = "New";
	public static final String GRID_STATUS_CONFIRMED = "Already Confirmed";
	public static final String GRID_STATUS_CENTERBTN = "CenterBtn";

	/************************** �α��� Ŀ�ǵ� ***************************/
	public static final String COMMAND_ADDCLIENT = "addClient";
	public static final String COMMAND_CHECKCLIENT = "checkClient";

	/************************** ���� Ŀ�ǵ� ***************************/
	public static final String COMMAND_FIRSTCONNECTION = "firstConnection";
	public static final String COMMAND_CREATEROOM = "createRoom";
	public static final String COMMAND_ENTERROOM = "enterRoom";
	public static final String COMMAND_DISCONNECTION = "disconnection";

	/********************** ���� �� ���� Ŀ�ǵ� **************************/

	public static final String COMMAND_ENTER = "enter";
	public static final String COMMAND_READY = "ready";
	public static final String COMMAND_START = "start";
	public static final String COMMAND_SENDMESSAGE = "sendMessage";
	public static final String COMMAND_EXIT = "exit";
	public static final String COMMAND_SUBMIT = "submit";
	public static final String COMMAND_SHUFFLE = "shuffle";
	public static final String COMMAND_END = "End";
}