package client;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class CreateRoom extends JDialog implements ActionListener {
	
	WaitingRoom wr;
	String id;
	
	JLabel roomCreate;
	JPanel pCenter;
	JPanel pName;
	JLabel name;
	JTextField tfName;
	JPanel pPW;
	JLabel pw;
	JPasswordField tfPW;
	JPanel pNum;
	JLabel num;
	JComboBox<String> cbNum;
	JPanel pRb;
	JLabel radioButton;
	ButtonGroup bg;
	JRadioButton rb7;
	JRadioButton rb11;
	JRadioButton rb15;
	JPanel pBtn;
	JButton btnCreate;
	JButton btnCancel;
	
	public CreateRoom() {
		init();
		
		this.setLayout(new BorderLayout(0, 20));
		this.add(roomCreate, "North");
		this.add(pCenter, "Center");
		this.add(pBtn, "South");
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
//				wr.setEnabled(true);
//				wr.requestFocus();
			}
		});
		
		setBounds(800, 400, 300, 300);
		setVisible(true);
	}
	
	public CreateRoom(WaitingRoom wr, String id) {
		// TODO Auto-generated constructor stub
		this.wr = wr;
		this.id = id;
		init();
		
		this.setLayout(new BorderLayout(0, 20));
		this.add(roomCreate, "North");
		this.add(pCenter, "Center");
		this.add(pBtn, "South");
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				wr.setEnabled(true);
				wr.requestFocus();
			}
		});
		
		setSize(300, 300);
		setLocationRelativeTo(wr);
		setVisible(true);
	}
	
	public void init() {
		// Create room
		roomCreate = new JLabel("Create room", JLabel.CENTER);
		roomCreate.setFont(new Font(null, Font.BOLD, 20));
		
		// center parts
		pCenter = new JPanel(new GridLayout(4, 1, 0, 10));
		pName = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		name = new JLabel("Room name  ", JLabel.CENTER);
		tfName = new JTextField(10);
		pName.add(name);
		pName.add(tfName);
		pPW = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		pw = new JLabel("Password", JLabel.CENTER);
		tfPW = new JPasswordField(10);
		pPW.add(pw);
		pPW.add(tfPW);
		pNum = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		num = new JLabel("Members   ", JLabel.CENTER);
		cbNum = new JComboBox<>();
		cbNum.addItem("          2 people          ");
		cbNum.addItem("          3 people          ");
		cbNum.addItem("          4 people          ");
		pNum.add(num);
		pNum.add(cbNum);
		
		pRb = new JPanel();
		radioButton = new JLabel("Size");
		rb7 = new JRadioButton("7X7", true);
		rb11 = new JRadioButton("11X11");
		rb15 = new JRadioButton("15X15");
		bg = new ButtonGroup();
		rb7.setName("7");
		rb11.setName("11");
		rb15.setName("15");
		pRb.add(radioButton);
		pRb.add(rb7);
		pRb.add(rb11);
		pRb.add(rb15);
		bg.add(rb7);
		bg.add(rb11);
		bg.add(rb15);
		
		pCenter.add(pName);
		pCenter.add(pPW);
		pCenter.add(pNum);
		pCenter.add(pRb);
		
		// Button
		pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		btnCreate = new JButton("Create");
		btnCancel = new JButton("Cancel");
		pBtn.add(btnCreate);
		pBtn.add(btnCancel);
		
		// Listener
		btnCreate.addActionListener(this);
		btnCancel.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnCreate) {
			System.out.println(tfName.getText());
			if (tfName.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Enter room name.");
			} else {				
				int roomNumber = 1;
				
				for (int i = 0; i < wr.dtm.getRowCount(); i++) {
					if ((Integer) wr.dtm.getValueAt(i, 0) == roomNumber) {
						roomNumber++;
					}
				}
				
				String cbSelected = (String) cbNum.getSelectedItem();
				int selectedSize = 0;
				if (rb7.isSelected()) {
					selectedSize = Integer.parseInt(rb7.getName());
				} else if (rb11.isSelected()) {
					selectedSize = Integer.parseInt(rb11.getName());
				} else if (rb15.isSelected()) {
					selectedSize = Integer.parseInt(rb15.getName());
				}
				
				String pw = new String(tfPW.getPassword(), 0, tfPW.getPassword().length);
				String roomName = tfName.getText();
				
				if (pw.equals("")) {
					roomName += " [Public room]";
				} else {
					roomName += " [Private room]";
				}
								
				ClientRoom r = new ClientRoom(roomNumber, roomName, pw ,selectedSize, Integer.parseInt(cbSelected.replaceAll("[^0-9]", "")));
				wr.createRoom(r);
				this.dispose();
				wr.setEnabled(false);
				new GameRoom(wr, id, roomNumber, selectedSize).setVisible(true);
			}
			
		} else if (e.getSource() == btnCancel) {
			this.dispose();
			wr.setEnabled(true);
			wr.requestFocus();
		} 
	}
}
