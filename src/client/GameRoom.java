package client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import javafx.scene.layout.Border;
//import server.DictionaryManager;
//import vo.ColWord;
//import vo.MyConstants;
//import vo.MyGameData;
//import vo.RowWord;
//import vo.Word;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import vo.ColWord;
import vo.MyConstants;
import vo.MyGameData;
import vo.RowWord;
import vo.Word;

public class GameRoom extends JFrame implements ActionListener, MouseListener {
	////////// class parts //////////////
	private Random random = new Random();
	private JFrame mainFrame = this;
	private GridGenerator grid;
	///////////////////////////////////////////////////////////
	private ClientManager cm;
	private WaitingRoom waitingRoom;
	private String id; // my id
	private int roomNo;
	private int gridSize; // When game starts, select map size

	private String[] word = { "R", "E", "A", "D", "Y", "P", "L", "Z", "!" };

	private char[] alphabet = { 'A', 'A', 'B', 'C', 'C', 'D', 'D', 'E', 'E', 'F', 'F', 'G', 'G', 'H', 'H', 'I', 'I',
			'J', 'K', 'L', 'L', 'M', 'M', 'N', 'N', 'O', 'O', 'P', 'P', 'Q', 'R', 'R', 'S', 'S', 'T', 'T', 'U', 'U',
			'V', 'W', 'X', 'Y', 'Y', 'Z' };

	private Color[] color = { Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE };

	////////////////////////////////////////////////////////////

	private ArrayList<Character> alphabetDeck;
	private ArrayList<JButton> myDeckList = new ArrayList<>();
	private ArrayList<JButton> usedMyDeck = new ArrayList<>();
	private ArrayList<JButton> selectedBtnListByMousePressed = new ArrayList<>();
	private ArrayList<int[]> selectedBtnXYListByMousePressed = new ArrayList<>(); // for sending to server
	private HashMap<int[], String> alphabetListbyMousePressed = new HashMap<>();
	private ArrayList<Character> myDeck;
	private MovingStrategy movingStrategy;
	private ArrayList<RowWord> row_wordList;
	private ArrayList<ColWord> col_wordList;
	private int[] xy;

	/////////////////////////////////////////////////////////////////
	private JLabel[] lbl_id;
	private JLabel[] lbl_score;
	//////////////////////////////////////////////////////////////////

	private boolean myDeckBtnisPressed;
	private boolean gridBtnisPressed;
	private boolean shuffleSign;
	private boolean rollbackSign;
	private boolean isTimeOut;
	//////////////////////////////////////////////////////////////////
	private JPanel contentPane;
	private JTextField tf_sendchat;
	private JPanel GridMap;
	private JScrollPane scrollPane;

	private JButton[][] coordinate;
	private JButton mousePressedBtn;
	private JButton btn_shuffle;
	private JButton btn_clear;
	private JButton btn_turnover;

	private JButton[] btn_deck = new JButton[9];

	private JButton btn_submit;
	private JButton btn_exit;
	private JButton btn_ready;
	private JLabel lbTimer;

	public JLabel getLbTimer() {
		return lbTimer;
	}

	private JLabel lblWord;

	private Thread t;
	private TimerThread timer;

	private JTextArea ta_chat;

	private ArrayList<String> userList;
	private JPanel panel_1;
	private JPanel panel_6;

	private boolean gameStarted;
	private boolean isMyTurn;

	public GameRoom() {
		id = "1";
		roomNo = '1';
		try {
			cm = new ClientManager("localhost", 9646);
			cm.setGr(this);
			cm.start();
			// this.DBsettings();
			this.basicSettings();
			// this.getGridLocation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public GameRoom(WaitingRoom waitingRoom, String id, int roomNo, int gridSize) {
		this.waitingRoom = waitingRoom;
		this.id = id;
		this.roomNo = roomNo;
		this.gridSize = gridSize;
		this.cm = waitingRoom.cm;
		cm.setGr(this);
		// cm.start(); // required to get back to game room
		this.Materials();
		this.basicSettings();
		this.getGridLocation();
		cm.enter(roomNo, id);
	}

	public void Materials() { //////////////////////////////////////////
		alphabetDeck = new ArrayList<>();
		for (char ch : alphabet) {
			alphabetDeck.add(ch);
		}
		myDeck = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			myDeck.add(alphabetDeck.get(random.nextInt(44)));
		}
	}

	public void basicSettings() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int answer = JOptionPane.showConfirmDialog(GameRoom.this, "Are you sure to exit? \n" + "Seriously?",
						"Exit Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (answer == 0) {
					exitRoom();
				}
			}
		});
		setSize(1250, 700);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		GridMap = new JPanel();
		contentPane.add(GridMap, BorderLayout.CENTER);
		GridMap.setLayout(new GridLayout(1, 1, 0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel panel_3 = new JPanel();
		panel.add(panel_3);

		JPanel panel_deck = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		panel_3.add(panel_deck);
		panel_deck.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(153, 180, 209), SystemColor.activeCaption,
				SystemColor.activeCaption, SystemColor.activeCaption));

		for (int i = 0; i < 9; i++) {
			btn_deck[i] = new JButton(word[i]);
			btn_deck[i].setFont(new Font("Vrinda", Font.BOLD, 20));
			btn_deck[i].addActionListener(this);
			myDeckList.add(btn_deck[i]);
			panel_deck.add(btn_deck[i]);
		}

		btn_submit = new JButton("TURNOVER");
		btn_submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((gameStarted && isMyTurn) || isTimeOut) {
					Word word = collection_PossibleWords();

					System.out.println("ROW");
					for (RowWord row : word.getRowwords()) {
						for (int[] ab : row.getRow_wordXYList()) {
							System.out.println(ab[0] + " " + ab[1]);
						}
					}
					System.out.println("COL");
					for (ColWord col : word.getColwords()) {
						for (int[] ab : col.getCol_wordXYList()) {
							System.out.println(ab[0] + " " + ab[1]);
						}
					}
					MyGameData gameData = new MyGameData(word, selectedBtnXYListByMousePressed,
							alphabetListbyMousePressed);
					System.out.println("MOUSE");
					for (int[] xy : selectedBtnXYListByMousePressed) {
						System.out.println(xy[0] + " " + xy[1]);
					}

					cm.submit(roomNo, id, gameData);

					isMyTurn = false;
					if (timer != null) {
						timer.setFlag(false);
					}

					// turnColor(id, false);
				} else if (!isMyTurn) {
					JOptionPane.showMessageDialog(GameRoom.this, "Not your turn.");
				} else {
					JOptionPane.showMessageDialog(GameRoom.this, "Game not started.");
				}
				System.out.println("===================================================");
			}
		});

		btn_shuffle = new JButton("SHUFFLE");
		btn_shuffle.addActionListener(new ActionListener() {/////////////////////////////////////////////

			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameStarted && isMyTurn) {
					if (shuffleSign) {
						JOptionPane.showMessageDialog(GameRoom.this, "One of your decks is clicked. \n "///
								+ "Please click the button(CLEAR) and then shuffle again.");///
					} else {
						int btnNo = 0;
						for (JButton btn : myDeckList) {
							if (btn.isEnabled()) {
								btn.setText(alphabetDeck.get(random.nextInt(44)).toString());///
								btn.setEnabled(true);
								btnNo++;
							}
						}
						int minusScore = -btnNo * 10; // Shuffle penalty: -10 points
						cm.shuffle(roomNo, id, minusScore);
					}
				} else if (!isMyTurn) {
					JOptionPane.showMessageDialog(GameRoom.this, "Not your turn.");
				} else {
					JOptionPane.showMessageDialog(GameRoom.this, "Game not started.");
				}
			}
		});
		btn_shuffle.setFont(new Font("Bold", Font.BOLD, 10));
		panel_2.add(btn_shuffle);

		btn_clear = new JButton("CLEAR");
		btn_clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameStarted && isMyTurn) {
					rollback();
					rollbackSign = false;
				} else if (!isMyTurn) {
					JOptionPane.showMessageDialog(GameRoom.this, "Not your turn.");
				} else {
					JOptionPane.showMessageDialog(GameRoom.this, "Game not started.");
				}
			}
		});
		btn_clear.setFont(new Font("Bold", Font.BOLD, 10));
		btn_clear.setForeground(Color.BLACK);
		panel_2.add(btn_clear);

		panel_3.add(btn_submit);
		btn_submit.setForeground(new Color(0, 128, 0));
		btn_submit.setFont(new Font("Vrinda", Font.BOLD, 20));

		btn_ready = new JButton("READY");
		btn_ready.setForeground(Color.ORANGE);
		btn_ready.setFont(new Font("Vrinda", Font.BOLD, 20));
		btn_ready.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isReady = false;
				if (btn_ready.getText().equals("READY")) {
					btn_ready.setText("CANCEL");
					isReady = true;
				} else {
					btn_ready.setText("READY");
					isReady = false;
				}

				cm.ready(roomNo, id, isReady);
			}
		});
		panel_3.add(btn_ready);

		btn_exit = new JButton("EXIT");
		btn_exit.setForeground(new Color(178, 34, 34));
		btn_exit.setFont(new Font("Vrinda", Font.BOLD, 20));
		btn_exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(GameRoom.this, "Are you sure to exit? \n" + "Seriously?",
						"Exit Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (answer == 0) {
					exitRoom();
				}
			}
		});
		panel.add(btn_exit);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panel_5, BorderLayout.EAST);
		panel_5.setLayout(new GridLayout(3, 2, 0, 0));

		panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_5.add(panel_6);
		panel_6.setLayout(new GridLayout(0, 2, 0, 2));

		panel_1 = new JPanel();
		panel_5.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_timer = new JPanel();
		panel_1.add(panel_timer);
		panel_timer.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_timer.setBackground(Color.WHITE);
		panel_timer.setLayout(new BorderLayout(0, 0));

		lbTimer = new JLabel("01:00");
		lbTimer.setFont(new Font("Dialog", Font.BOLD, 40));
		lbTimer.setHorizontalAlignment(SwingConstants.CENTER);
		panel_timer.add(lbTimer);

		JPanel panel_word = new JPanel();
		panel_1.add(panel_word);
		panel_word.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_word.setBackground(Color.WHITE);
		panel_word.setLayout(new BorderLayout(0, 0));

		lblWord = new JLabel("Word");
		lblWord.setFont(new Font("Dialog", Font.BOLD, 40));
		lblWord.setHorizontalAlignment(SwingConstants.CENTER);
		panel_word.add(lblWord, BorderLayout.CENTER);

		JPanel panel_8 = new JPanel();
		panel_5.add(panel_8);
		panel_8.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panel_8.add(scrollPane, BorderLayout.CENTER);

		ta_chat = new JTextArea();
		ta_chat.setEditable(false);
		ta_chat.setLineWrap(true);
		scrollPane.setViewportView(ta_chat);

		tf_sendchat = new JTextField();
		panel_8.add(tf_sendchat, BorderLayout.SOUTH);
		tf_sendchat.setColumns(20);
		tf_sendchat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String message = tf_sendchat.getText();

				if (message.isEmpty()) {
					return;
				}
				cm.sendMessage(roomNo, id, message);
				tf_sendchat.setText("");
			}
		});

	}

	public Word collection_PossibleWords() {
		row_wordList = collect_Row_PossibleWords();
		col_wordList = collect_Col_PossibleWords();
		Word word = new Word(row_wordList, col_wordList);
		return word;
	}

	public ArrayList<RowWord> collect_Row_PossibleWords() {
		ArrayList<RowWord> result = new ArrayList<>();
		//init to make new buffer to collect word info.
		RowWord rowWord = new RowWord(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

		boolean block = false;
		System.out.println("Row inspection starts");
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				// first inspection
				// GRID_SIGN_NEW : inserted newly by user, GRID_SIGN_CONFIRMED: already used by others
				if (coordinate[x][y].getName().equals(MyConstants.GRID_STATUS_NEW)
						|| coordinate[x][y].getName().equals(MyConstants.GRID_STATUS_CONFIRMED)) {
					block = true; // boolean sign for the secondary inspection
				} else { // the other case that nobody used, which means that it is empty space
					if (rowWord.getWordSize() >= 2) { // Minimum size of ENG word is consist of two alphabets at least
						result.add(rowWord);
					}
					// if size of the word is less than 2, it means this is not a word
					block = false; // boolean sign => this is not a word so secondary inspection is not needed
					//init to make new buffer to collect info.
					rowWord = new RowWord(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
				}
				// sencondary inspection
				if (block) { // combine the consecutive words
					// Status records of each grids for server to check if each word is new one
					rowWord.addNorC_CombinationList((coordinate[x][y].getName())); 
					// coordinate records
					rowWord.addBtn(new int[] { x, y }); 
					// searched alphabet records
					rowWord.addWordNameList((coordinate[x][y].getText()));
				}
				// final inspection
				// to check if y coordinate is the last line of the row
				// if it is true, it is no longer consecutive so it should be saved as one complete word
				if (y == gridSize - 1) {
					if (rowWord.getWordSize() >= 2) { // Minimum size of ENG word is consist of two alphabets at least
						result.add(rowWord);
					}
					// init to make new buffer to collect info
					rowWord = new RowWord(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

				}
			}
		}
		StringBuffer buffer = new StringBuffer("[ROW] Found unconfirmed Possible Word: ");
		for (RowWord rw : result) {
			buffer.append(rw.showWord() + ", ");
		}
		System.out.println(buffer);

		return result;
	}

	public ArrayList<ColWord> collect_Col_PossibleWords() {
		ArrayList<ColWord> result = new ArrayList<>();
		//init to make new buffer to collect word info.
		ColWord colWord = new ColWord(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

		boolean block = false;
		System.out.println("Column inspection starts");
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				// first inspection
				// GRID_SIGN_NEW : inserted newly by user, GRID_SIGN_CONFIRMED: already used by others
				if (coordinate[y][x].getName().equals(MyConstants.GRID_STATUS_NEW)
						|| coordinate[y][x].getName().equals(MyConstants.GRID_STATUS_CONFIRMED)) {
					block = true; // boolean sign for the secondary inspection
				} else { // the other case that nobody used, which means that it is empty space
					if (colWord.getWordSize() >= 2) { // Minimum size of ENG word is consist of two alphabets at least
						result.add(colWord);
					}
					// if size of the word is less than 2, it means this is not a word
					block = false; // boolean sign => this is not a word so secondary inspection is not needed
					//init to make new buffer to collect info.
					colWord = new ColWord(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

				}
				// sencondary inspection
				if (block) { // combine the consecutive words
					// Status records of each grids for server to check if each word is new one
					colWord.addNorC_CombinationList((coordinate[y][x].getName())); 
					// coordinate records
					colWord.addBtn(new int[] { y, x }); 
					// searched alphabet records
					colWord.addWordNameList((coordinate[y][x].getText()));
				}
				// final inspection
				// to check if y coordinate is the last line of the row
				// if it is true, it is no longer consecutive so it should be saved as one complete word
				if (y == gridSize - 1) {
					if (colWord.getWordSize() >= 2) { // Minimum size of ENG word is consist of two alphabets at least
						result.add(colWord);
					}
					// init to make new buffer to collect info
					colWord = new ColWord(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

				}
			}
		}
		StringBuffer buffer = new StringBuffer("[Column] Found unconfirmed Possible Word: ");
		for (ColWord cw : result) {
			buffer.append(cw.showWord() + ", ");
		}
		System.out.println(buffer);

		return result;
	}

	public void updateGameData(String currentUserid, MyGameData returnData) {
		ArrayList<int[]> row_resultXY = returnData.getRow_resultXY();
		ArrayList<int[]> col_resultXY = returnData.getCol_resultXY();

		ArrayList<String> answerCollection = returnData.getAnswerCollection();
		HashMap<int[], String> row_inputAlphabets = returnData.getRow_stringCollectionMap();
		HashMap<int[], String> col_inputAlphabets = returnData.getCol_stringCollectionMap();

		int userIndex = 0;

		for (int i = 0; i < userList.size(); i++) {
			// lbl_id[i].setOpaque(true);
			// lbl_id[i].setBackground(Color.WHITE);
			// lbl_score[i].setOpaque(true);
			// lbl_score[i].setBackground(Color.WHITE);

			if (userList.get(i).equals(currentUserid)) {
				userIndex = i;
			}
		}

		// lbl_id[userIndex].setOpaque(true);
		// lbl_id[userIndex].setBackground(Color.PINK);
		// lbl_score[userIndex].setOpaque(true);
		// lbl_score[userIndex].setBackground(Color.PINK);

		for (int[] xy : row_resultXY) {
			coordinate[xy[0]][xy[1]].setEnabled(false);
			coordinate[xy[0]][xy[1]].setName(MyConstants.GRID_STATUS_CONFIRMED);
			String row_str = null;
			row_str = row_inputAlphabets.get(xy);
			row_inputAlphabets.remove(xy);
			if (row_str != null) {
				coordinate[xy[0]][xy[1]].setText(row_str);
			}
			coordinate[xy[0]][xy[1]].setBackground(color[userIndex]);
		}
		for (int[] xy : col_resultXY) {
			coordinate[xy[0]][xy[1]].setEnabled(false);
			coordinate[xy[0]][xy[1]].setName(MyConstants.GRID_STATUS_CONFIRMED);
			String col_str = null;
			col_str = col_inputAlphabets.get(xy);
			col_inputAlphabets.remove(xy);
			if (col_str != null) {
				coordinate[xy[0]][xy[1]].setText(col_str);
			}
			coordinate[xy[0]][xy[1]].setBackground(color[userIndex]);
		}

		makeNextAvailableBtnAfterChecking();

		int plusScore = returnData.getPlusScore();

		if (plusScore == 0 && currentUserid.equals(id)) {
			// if (gridBtnisPressed && myDeckBtnisPressed) {
			if (rollbackSign) {
				rollback();
				JOptionPane.showMessageDialog(mainFrame, "Sorry, Nothing correct. Turnover", "Alert", 1);
				rollbackSign = false;
			} else {
				JOptionPane.showMessageDialog(mainFrame, "Turnover", "Alert", 1);
			}

			int randomNum = random.nextInt(44);
			for (JButton btn : btn_deck) {
				if (!btn.isEnabled()) {
					btn.setEnabled(true);
					btn.setText(alphabetDeck.get(randomNum).toString());
					break;
				}
			}
		}

		lblWord.setFont(new Font(null, Font.BOLD, 48 - answerCollection.size() * 2)); //
		if (plusScore != 0 && currentUserid.equals(id)) {
			StringBuffer strBuffer = new StringBuffer();
			for (String str : answerCollection) {
				strBuffer.append(str + " ");
			}
			for (String str : answerCollection) {
				lblWord.setText(strBuffer.toString().toUpperCase());///
			} //
			JOptionPane.showMessageDialog(mainFrame, "Good job! \n Your Correct Words are shown on your right side",
					"INFO", JOptionPane.INFORMATION_MESSAGE);
			strBuffer = new StringBuffer();////
		}

		int deckBtn = 0;

		for (JButton btn : btn_deck) {
			if (!btn.isEnabled()) {
				deckBtn++;
			}
		}

		if (currentUserid.equals(id)) {
			if (deckBtn == 9 || Integer.parseInt(lbl_score[userIndex].getText()) >= 100) {
				cm.gameEnd(roomNo, currentUserid);
				JOptionPane.showMessageDialog(mainFrame, "Game Ended!! YOU WON. Congratualations!!");
			}
		}

		selectedBtnXYListByMousePressed.clear();
		selectedBtnListByMousePressed.clear();
		usedMyDeck.clear();
		shuffleSign = false;
	}

	public void gameLost(String winnerId) {
		if (!id.equals(winnerId)) {
			JOptionPane.showMessageDialog(mainFrame, "Game Ended!! YOU LOST. Winner is " + winnerId);
		}
		timer.setFlag(false);
		gameStarted = false;
		btn_ready.setEnabled(true);
	}

	public void rollback() {
		System.out.println("DB Inspection Result: Now words so rollbacked");
		System.out.println("===================================================");
		shuffleSign = false;

		for (JButton btn : selectedBtnListByMousePressed) { // init all of components
			btn.setEnabled(false);
			btn.setText("");
			btn.setName(MyConstants.GRID_STATUS_NORMAL);
			btn.setBackground(null);
		}

		makeNextAvailableBtnAfterChecking();

		for (JButton btn : usedMyDeck) { // init decks

			btn.setEnabled(true);
		}
		selectedBtnListByMousePressed.clear();
		selectedBtnXYListByMousePressed.clear();
		usedMyDeck.clear();

		btn_submit.setText("TURNOVER");
	}

	public void getGridLocation() {
		grid = new GridGenerator(gridSize, gridSize);
		coordinate = new JButton[gridSize][gridSize];
		int i = 0;
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				coordinate[x][y] = grid.btns[x][y];
				coordinate[x][y].addMouseListener(this);
				coordinate[x][y].setFont(new Font(null, Font.BOLD, 54 - 2 * gridSize));
				coordinate[x][y].setName(MyConstants.GRID_STATUS_NORMAL); // create asc number component name in grid
			}
		}
		coordinate[gridSize / 2][gridSize / 2].setName(MyConstants.GRID_STATUS_CENTERBTN);
		GridMap.add(grid);
		movingStrategy = new MovingStrategy(coordinate, gridSize);
	}

	// public void turnColor(String currentUserid, boolean isStart) {
	//
	// int userIndex = 0;
	//
	// if (isStart) {
	// for (int i = 0; i < userList.size(); i++) {
	// lbl_id[i].setOpaque(true);
	// lbl_id[i].setBackground(Color.WHITE);
	// lbl_score[i].setOpaque(true);
	// lbl_score[i].setBackground(Color.WHITE);
	//
	// if (userList.get(i).equals(currentUserid)) {
	// userIndex = i;
	// }
	// }
	//
	// lbl_id[userIndex].setOpaque(true);
	// lbl_id[userIndex].setBackground(Color.PINK);
	// lbl_score[userIndex].setOpaque(true);
	// lbl_score[userIndex].setBackground(Color.PINK);
	// } else {
	// if (userList.indexOf(id) + 1 == userList.size()) {
	// userIndex = 0;
	// } else {
	// userIndex = userList.indexOf(id);
	//
	// lbl_id[userIndex].setOpaque(true);
	// lbl_id[userIndex].setBackground(Color.PINK);
	// lbl_score[userIndex].setOpaque(true);
	// lbl_score[userIndex].setBackground(Color.PINK);
	// }
	// }
	//
	// }

	public void initALL() {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				coordinate[i][j].setEnabled(false);
				coordinate[i][j].setName(MyConstants.GRID_STATUS_NORMAL);
				coordinate[i][j].setText("");
				coordinate[i][j].setBackground(null);
			}
		}
	}

	public void makeNextAvailableBtn() {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (!coordinate[i][j].getText().equals("")) {
					movingStrategy.nextStep(i, j, true);
				}
			}
		}
	}

	public void makeNextAvailableBtnAfterChecking() {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (!coordinate[i][j].getName().equals(MyConstants.GRID_STATUS_CONFIRMED)) {
					coordinate[i][j].setEnabled(false);
					coordinate[i][j].setName(MyConstants.GRID_STATUS_NORMAL);
					coordinate[i][j].setText("");
					coordinate[i][j].setBackground(null);
				}
			}
		}

		boolean centerBtn = true;
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (coordinate[i][j].getName().equals(MyConstants.GRID_STATUS_CONFIRMED)) {
					movingStrategy.nextStep(i, j, true);
					centerBtn = false;
				}
			}
		}

		if (centerBtn) {
			coordinate[gridSize / 2][gridSize / 2].setEnabled(true);
		}
	}

	// public void getXY(JButton selectedBtn) {
	// for (int i = 0; i < gridSize; i++) {
	// for (int j = 0; j < gridSize; j++) {
	// if(selectedBtn == coordinate[i][j]) {
	// selectedBtnX = i; selectedBtnY = j;
	// return;
	// }
	// }
	// }
	// }

	public void allListClear() {
		myDeck.clear();
		usedMyDeck.clear();
		selectedBtnListByMousePressed.clear();
		selectedBtnXYListByMousePressed.clear();
		alphabetListbyMousePressed.clear();
	}

	public void showMessageEnteredRoom() { ///////
		// TODO Auto-generated method stub
		String enteredUser = userList.get(userList.size() - 1);
		ta_chat.append("[ " + enteredUser + " ] " + " entered.\n");
	}

	public void gameStart(boolean start, String turnId/* , int[] deckNum[] ! */) {
		if (start) {

			// turnColor(turnId, true);

			if (turnId.equals(id)) {
				myTurn();

			}

			initALL();

			for (int i = 0; i < userList.size(); i++) {
				lbl_score[i].setText("0");
			}

			for (int i = 0; i < 9; i++) {
				btn_deck[i].setText(word[i]);
			}

			allListClear();

			gameStarted = true;

			for (int i = 0; i < 9; i++) {
				int randomNum = random.nextInt(44);
				btn_deck[i].setText(alphabetDeck.get(randomNum).toString());
			}

			ta_chat.append("game start!\n");

			JOptionPane.showMessageDialog(mainFrame, "GAME STARTS!!!!!", "GAME ANNOUNCEMENT",
					JOptionPane.INFORMATION_MESSAGE);

			waitingRoom.dtm.setValueAt("In Game", roomNo - 1, 4);
			cm.gameStart(roomNo);
			btn_ready.setText("READY");
			btn_ready.setEnabled(false);
			btn_submit.setEnabled(true);

			// Enable game button
			coordinate[gridSize / 2][gridSize / 2].setEnabled(true);

			/****************************************************************************************************************************************/

		}
	}

	public void myTurn() {
		// TODO Auto-generated method stub
		isMyTurn = true;
		timer = new TimerThread(this);
		t = new Thread(timer);
		t.start();
	}

	public void submit(String currentId, String nextId, MyGameData returnData) {
		inputScore(currentId, returnData.getPlusScore());

		// turnColor(currentId, false);

		updateGameData(currentId, returnData);
		btn_submit.setText("TURNOVER"); // submit prepared word

		if (id.equals(nextId) && gameStarted) {
			myTurn(); // stop timer thread

			JOptionPane.showMessageDialog(GameRoom.this, "Your Turn starts!!", "Turn Alarm",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void shuffle(String currentId, int minusScore) {
		inputScore(currentId, minusScore);
	}

	public void inputScore(String id, int plusScore) {
		int userIndex = 0;

		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).equals(id)) {
				userIndex = i;
			}
		}

		int score = Integer.parseInt(lbl_score[userIndex].getText());
		score += plusScore;

		lbl_score[userIndex].setText(Integer.toString(score));
	}

	public void showMessage(String id, String message) {
		// TODO Auto-generated method stub
		ta_chat.append("[" + id + "]" + message + "\n");
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}

	public void exitRoom() {
		cm.exit(roomNo, id);
		this.dispose();
		waitingRoom.setEnabled(true);
		waitingRoom.requestFocus();
	}

	public void refreshUserList(ArrayList<String> newUserList) {
		userList = new ArrayList<>();
		for (int i = 0; i < newUserList.size(); i++) {
			userList.add(newUserList.get(i));
		}
	}

	public void dynamicLabelMake() {

		lbl_id = new JLabel[4];
		lbl_score = new JLabel[4];

		panel_6.removeAll();
		for (int i = 0; i < userList.size(); i++) {
			lbl_id[i] = new JLabel(userList.get(i));
			panel_6.add(lbl_id[i]);
			lbl_id[i].setHorizontalAlignment(SwingConstants.CENTER);
			lbl_id[i].setBackground(color[i]);
			lbl_id[i].setFont(new Font("Vrinda", Font.BOLD, 30 + 5 * (4 - userList.size())));
			lbl_id[i].setForeground(color[i]);

			lbl_score[i] = new JLabel("0");
			lbl_score[i].setFont(new Font("Vrinda", Font.BOLD, 30 + 5 * (4 - userList.size())));
			panel_6.add(lbl_score[i]);
			lbl_score[i].setHorizontalAlignment(SwingConstants.CENTER);
		}
	}

	public void timeOut() {
		// TODO Auto-generated method stub
		btn_submit.setText("TURNOVER");
		JOptionPane.showMessageDialog(this, "Time's up. Please press the turnover button.");
		isTimeOut = true;
		// rollback();
	}

	/**********************************
	 * ���� �߰��ϴ� �޼ҵ� ��
	 **********************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gridBtnisPressed && myDeckBtnisPressed) {
			for (int i = 0; i < 9; i++) {
				if (e.getSource() == btn_deck[i]) {
					word[i] = btn_deck[i].getText();
					mousePressedBtn.setText(word[i]);
					btn_deck[i].setEnabled(false);
					usedMyDeck.add(btn_deck[i]);
					alphabetListbyMousePressed.put(xy, word[i]);
				}
			}
			mousePressedBtn.setName(MyConstants.GRID_STATUS_NEW);
			mousePressedBtn.setEnabled(false);
			mousePressedBtn.setBackground(Color.BLUE);
			btn_submit.setText("SUBMIT"); // change button text
			makeNextAvailableBtn();
			mousePressedBtn = null;
			gridBtnisPressed = false;
			// myDeckBtnisPressed = false;
			shuffleSign = true;
			rollbackSign = true;
			return;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameStarted && isMyTurn) {
			if (e.getSource() instanceof JButton) {
				myDeckBtnisPressed = true;
				mousePressedBtn = (JButton) e.getSource();
				for (int i = 0; i < gridSize; i++) {
					for (int j = 0; j < gridSize; j++) {
						if (mousePressedBtn == coordinate[i][j]) {
							if (mousePressedBtn.isEnabled() && mousePressedBtn.getText().equals("")) { // store button
																										// grid address
								xy = new int[] { i, j };
								if (selectedBtnListByMousePressed.isEmpty()) {
									selectedBtnListByMousePressed.add(mousePressedBtn);
									selectedBtnXYListByMousePressed.add(xy); // for server
									gridBtnisPressed = true;
									break;
								} else {
									for (JButton btn : selectedBtnListByMousePressed) {
										if (mousePressedBtn != btn) {
											selectedBtnListByMousePressed.add(mousePressedBtn);
											selectedBtnXYListByMousePressed.add(xy); // for server
										}
										gridBtnisPressed = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		} else if (gameStarted && !isMyTurn) {
			JOptionPane.showMessageDialog(GameRoom.this, "Not your turn.");
		} else if (!gameStarted) {
			JOptionPane.showMessageDialog(GameRoom.this, "Game not started.");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
