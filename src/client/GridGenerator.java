package client;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GridGenerator extends JPanel {
	public JButton[][] btns;

	public GridGenerator(int x, int y) {
		btns = new JButton[x][y];
		this.setLayout(new GridLayout(x, y));
		this.createTF(x, y);
	}

	// public int randomOption() { // remove random option
	// return 0;
	// }

	public void createTF(int x, int y) {
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				btns[i][j] = new JButton();
				this.add(btns[i][j]);
				btns[i][j].setEnabled(false);
			}
		}
		// btns[(x-1)/2][(y-1)/2].setEnabled(false);
		// btns[(x-1)/2][(y-1)/2].setName(MyConstants.GRID_STATUS_CONFIRMED); // set
		// start grid
	}
}
