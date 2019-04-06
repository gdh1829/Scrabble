package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class WaitingRoom extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	String id;
	ClientManager cm;

	JScrollPane pRoomList;
	JScrollPane pUserList;
	JScrollPane spChat;
	JPanel pChat;
	JPanel pUser;
	JPanel pBtn;
	JList<String> userList;
	DefaultListModel<String> dlm;
	DefaultTableModel dtm;
	JTable roomTable;
	JTextArea chat;
	JTextField ch;
	JButton btnCreate;
	JButton btnEnter;
	JButton btnExit;
	
	int tempSelectedRow;
	
//	TestRoom tr;
	
	public WaitingRoom(ClientManager cm, String id) {
		super("Waiting Room");
		this.cm = cm;
		this.id = id;
		cm.setWr(this);
		
		init();
		listener();
		setGridBagLayout();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}
		});
		this.setSize(1000, 600);
		this.setLocationRelativeTo(null);		
		this.setVisible(true);
		//////////////////////// Thread //////////////////////////////		
		cm.start();
		cm.firstConnection(id);
	}
	
	public void init() {
		// Chat space
		pChat = new JPanel(new BorderLayout(0, 5));
		ch = new JTextField();
		chat = new JTextArea();
		chat.setLineWrap(true);
		chat.setEditable(false);
		spChat = new JScrollPane(chat);
		spChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		spChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pChat.add(spChat, BorderLayout.CENTER);
		pChat.add(ch, BorderLayout.SOUTH);
		
		// User list
		pUser = new JPanel(new BorderLayout());
		JLabel ul = new JLabel("Users");
		dlm = new DefaultListModel<>();
		userList = new JList<>(dlm);
		pUserList = new JScrollPane(userList);
		pUser.add(ul, BorderLayout.NORTH);
		pUser.add(pUserList);
		
		// Room list
		String[] columnNames = { "Room No.", "Room Name", "Size", "Total User", "Current Status" };
		dtm = new DefaultTableModel(columnNames, 0) {
			
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column) {
				if (column == 4) {
					return true;
				}
				return false;
			}
		};
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		roomTable = new JTable(dtm);
		roomTable.getTableHeader().setReorderingAllowed(false); // Column header
		roomTable.getTableHeader().setResizingAllowed(false); // COlumn header size
		roomTable.setShowHorizontalLines(false); // Table horizon line
//		roomTable.setIntercellSpacing(new Dimension(0, 10)); // spaces among cells
		roomTable.setFont(new Font(null, Font.PLAIN, 20)); // char size of cells
		roomTable.setRowHeight(40); // height of cells
		roomTable.getColumnModel().getColumn(0).setPreferredWidth(10); // Room number size
		roomTable.getColumnModel().getColumn(1).setPreferredWidth(400); // Room name size
		roomTable.getColumnModel().getColumn(3).setPreferredWidth(50); // Total user size
		roomTable.getColumnModel().getColumn(4).setPreferredWidth(50);
		pRoomList = new JScrollPane(roomTable);
		
		for (int i = 0; i < roomTable.getColumnModel().getColumnCount(); i++) {
			roomTable.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		
		// Button List
		pBtn = new JPanel();
		pBtn.setLayout(new GridLayout(4, 1, 0, 10));
		btnCreate = new JButton("Create Room");
		btnEnter = new JButton("Enter Room");
		btnExit = new JButton("Exit");
		pBtn.add(new JLabel(""));
		pBtn.add(btnCreate);
		pBtn.add(btnEnter);
		pBtn.add(btnExit);
	}
	
	public void listener() {
		ch.addActionListener(this);
		btnCreate.addActionListener(this);
		btnEnter.addActionListener(this);
		btnExit.addActionListener(this);
		
		roomTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					tempSelectedRow = roomTable.getSelectedRow();
				} else if (e.getClickCount() == 2) { 
			    	enterRoom(tempSelectedRow);
			    }
			}
		});
	}
	
	public void setGridBagLayout() {
		GridBagLayout gridbag = new GridBagLayout();
		
		this.setLayout(gridbag);
		
		GridBagConstraints constraint = new GridBagConstraints();
		
		constraint.fill = GridBagConstraints.BOTH;
		
		// Chat
		constraint.insets = new Insets(10, 10, 10, 10);
		constraint.weightx = 10;
		constraint.weighty = 2;
		gridbag.setConstraints(pChat, constraint);
		getContentPane().add(pChat);
		
		// User list
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.weightx = 1;
		gridbag.setConstraints(pUser, constraint);
		getContentPane().add(pUser);
		
		// Room list
		constraint.gridwidth = 1;
		constraint.gridheight = 2;
		constraint.weighty = 1;
		gridbag.setConstraints(pRoomList, constraint);
		getContentPane().add(pRoomList);
		
		// Button list
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.gridheight = 1;
		constraint.weighty = 1;
		gridbag.setConstraints(pBtn, constraint);
		getContentPane().add(pBtn);		
	}
	
	// Chat room message
	public void showMessage(String id, String message) {
		chat.append("[" + id + "]" + message + "\n");
		spChat.getVerticalScrollBar().setValue(spChat.getVerticalScrollBar().getMaximum());
	}
	
	public void createRoom(ClientRoom r) {
		cm.createRoom(id, r);
	}
	
	public void enterRoom(int temp) {
		if (roomTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "Choose the game room.");
		} else if (((String) dtm.getValueAt(temp, 4)).equals("In Game")) {
			JOptionPane.showMessageDialog(this, "This room has already started.");
		} else {
			String selectedUserCount = (String) dtm.getValueAt(temp, 3);
			
			if (selectedUserCount.charAt(0) == selectedUserCount.charAt(2)) {	// When no empty space for users
				JOptionPane.showMessageDialog(this, "Full");
			} else {
				String selectedSize = (String) dtm.getValueAt(temp, 2);
				int size = Integer.parseInt((selectedSize.split("X")[0]));
				String roomName = (String) dtm.getValueAt(temp, 1);
				if (roomName.charAt(roomName.length() - 5) == 'Y') {
					cm.getRoomPW((int) dtm.getValueAt(temp, 0));
				} else {
					cm.enterRoom(id, (int) dtm.getValueAt(temp, 0));
					new GameRoom(this, id, (int) dtm.getValueAt(temp, 0), size).setVisible(true);
					this.setEnabled(false);					
				}
				
			}
		}
	}
	
	public void pwChecker(ClientRoom r) {
		String inputPW = JOptionPane.showInputDialog("Enter password");
		int roomNumber = r.getRoomNumber();
		String roomPW = r.getPw();
		int size = r.getSize();
		if (!roomPW.equals(inputPW)) {
			JOptionPane.showMessageDialog(this, "Wrong room password");
		} else {
			cm.enterRoom(id, (int) dtm.getValueAt(tempSelectedRow, 0));
			new GameRoom(this, id, roomNumber, size).setVisible(true);
			this.setEnabled(false);	
		}
	}
	
	public void disconnect() {
		cm.disconnection(id);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ch) {
			String message = ch.getText();
			if (message.isEmpty()) {
				return;
			}
			cm.sendMessage(id, ch.getText());
			ch.setText("");
			
		} else if (e.getSource() == btnCreate) {
			new CreateRoom(this, id);
			this.setEnabled(false);
		} else if (e.getSource() == btnEnter) {
			enterRoom(tempSelectedRow);
		} else if (e.getSource() == btnExit) {
			disconnect();
			this.dispose();
		}
	}
	
	public void refreshUserList(ArrayList<String> userList) {
		dlm.removeAllElements();
		for (int i = 0; i < userList.size(); i++) {
			dlm.addElement(userList.get(i));
		}
	}
	
	public void refreshRoomList(ArrayList<ClientRoom> rList) {
		dtm.setNumRows(0);
		for (int i = 0; i < rList.size(); i++) {
			ClientRoom room = rList.get(i);
			String state;
						
			if (room.isState()) {
				state = "In Game";
			} else {
				state = "Waiting";
			}
						
			dtm.addRow(new Object[] { room.getRoomNumber(), room.getRoomName(),
					room.getSize() + "X" + room.getSize(), room.getUserCount() + "/" + room.getpCount(), state });
		}
		
		// Adds JTable button
		/***********************************************/
//		JTableButton jtb = new JTableButton();
//		roomTable.getColumn("").setCellRenderer(jtb);
//		roomTable.getColumn("").setCellEditor(jtb.new ButtonEditor(new JCheckBox()));
		/***********************************************/
	}
	
	/*public void sendReadyMessage() {
		Object[] obj = { "ready", id, 1 };
		
		try {
			output.writeObject(obj);
			output.reset();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, "Failed to send messages");
		}
	}*/	
}
