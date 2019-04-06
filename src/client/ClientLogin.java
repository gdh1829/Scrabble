package client;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientLogin extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private ClientManager cm;

	private JTextField tfID; // id text field
	private JPasswordField tfPW; // password text field
	private JButton btnReg;
	private JButton btnConfirm;
	private JButton btnCancel;

	public ClientLogin() {
		super("Scrabble Login");

		this.connect();
		this.init();
		this.listener();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}
		});

		this.setSize(400, 120);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void connect() {
		try {
			// cm = new ClientManager("203.233.196.69", 9677);
			cm = new ClientManager("localhost", 9633);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Faield to access server");
			System.exit(0);
		}
	}

	public void init() { // Screen composition
		tfID = new JTextField(10); // id text field
		tfPW = new JPasswordField(10); // password text field
		btnReg = new JButton("Regist");
		btnConfirm = new JButton("Confirm");
		btnCancel = new JButton("Cancel");

		// Label Panel
		JPanel pn1 = new JPanel(new FlowLayout());
		pn1.add(new JLabel("ID"));
		pn1.add(tfID);
		pn1.add(new JLabel("Password"));
		pn1.add(tfPW);

		// Button Panel
		JPanel pn2 = new JPanel(new FlowLayout());
		pn2.add(btnReg);
		pn2.add(btnConfirm);
		pn2.add(btnCancel);

		this.setLayout(new GridLayout(2, 1));
		this.add(pn1);
		this.add(pn2);

	}

	public void listener() { // �̺�Ʈ
		tfPW.addActionListener(this);
		btnReg.addActionListener(this);
		btnConfirm.addActionListener(this);
		btnCancel.addActionListener(this);
	}

	public void disconnect() {
		try {
			cm.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error occurs when disconnecting");
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) { // ActionListener implments

		if (e.getSource() == btnReg) { // Member regist
			if (tfID.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Enter ID.");
			} else if (tfPW.getPassword().equals("")) {
				JOptionPane.showMessageDialog(this, "Enter Password.");
			} else {
				if (cm.addClient(tfID.getText(), tfPW.getPassword())) {
					JOptionPane.showMessageDialog(this, "Succeeded in sigining up for.");
				} else {
					JOptionPane.showMessageDialog(this, "Duplicated ID.");
				}
			}

		} else if (e.getSource() == tfPW || e.getSource() == btnConfirm) { // When click confirm button
			if (tfID.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Enter ID.");
			} else if (tfPW.getPassword().equals("")) {
				JOptionPane.showMessageDialog(this, "Enter Password.");
			} else {
				int check = cm.checkClient(tfID.getText(), tfPW.getPassword());
				if (check == 0) {
					JOptionPane.showMessageDialog(this, "No member exists.");
				} else if (check == 1) {
					JOptionPane.showMessageDialog(this, "Wrong password.");
				} else if (check == 2) {
					JOptionPane.showMessageDialog(this, "Duplicated login.");
				} else {
					JOptionPane.showMessageDialog(this, "Login success.");
					new WaitingRoom(cm, tfID.getText()); // Move to waiting room
					this.dispose();
				}
			}

		} else if (e.getSource() == btnCancel) { // When click cancel button
			disconnect();
			this.dispose();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ClientLogin();
	}
}
