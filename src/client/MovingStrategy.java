package client;

import javax.swing.JButton;

public class MovingStrategy {
	private JButton[][] coordinate;
	private int gridSize;
	
	
	public MovingStrategy(JButton[][] coordinate, int gridSize) {
		this.coordinate = coordinate;
		this.gridSize = gridSize;
	}
	
	public void nextStep(int x, int y, boolean true_or_false) {
		int limit = gridSize-1;
		if((x > 0 && x < limit) && (y > 0 && y < limit)) {
			if(coordinate[x-1][y].getText().equals("")) {
				coordinate[x-1][y].setEnabled(true_or_false);
			} 
			if(coordinate[x+1][y].getText().equals("")) {
				coordinate[x+1][y].setEnabled(true_or_false);
			}
			if(coordinate[x][y+1].getText().equals("")) {
				coordinate[x][y+1].setEnabled(true_or_false);
			}
			if(coordinate[x][y-1].getText().equals("")) {
				coordinate[x][y-1].setEnabled(true_or_false);
			}
		} else if (x == 0) {
			if(y == 0) {
				if(coordinate[x+1][y].getText().equals("")) {
					coordinate[x+1][y].setEnabled(true_or_false);
				} 
				if(coordinate[x][y+1].getText().equals("")) {
					coordinate[x][y+1].setEnabled(true_or_false);
				}
			} else if(y == limit) {
				if(coordinate[x+1][y].getText().equals("")) {
					coordinate[x+1][y].setEnabled(true_or_false);
				} 
				if(coordinate[x][y-1].getText().equals("")) {
					coordinate[x][y-1].setEnabled(true_or_false);
				}
			} else if(y > 0 && y < limit) {
				if(coordinate[x+1][y].getText().equals("")) {
					coordinate[x+1][y].setEnabled(true_or_false);
				} 
				if(coordinate[x][y-1].getText().equals("")) {
					coordinate[x][y-1].setEnabled(true_or_false);
				} 
				if(coordinate[x][y+1].getText().equals("")) {
					coordinate[x][y+1].setEnabled(true_or_false);
				}
			}
		} else if (x == limit) {
			if(y == 0) {
				if(coordinate[x-1][y].getText().equals("")) {
					coordinate[x-1][y].setEnabled(true_or_false);
				} 
				if(coordinate[x][y+1].getText().equals("")) {
					coordinate[x][y+1].setEnabled(true_or_false);
				}
			} else if(y == limit) {
				if(coordinate[x-1][y].getText().equals("")) {
					coordinate[x-1][y].setEnabled(true_or_false);
				} 
				if(coordinate[x][y-1].getText().equals("")) {
					coordinate[x][y-1].setEnabled(true_or_false);
				}
			} else if(y > 0 && y < limit) {
				if(coordinate[x-1][y].getText().equals("")) {
					coordinate[x-1][y].setEnabled(true_or_false);
				} 
				if(coordinate[x][y-1].getText().equals("")) {
					coordinate[x][y-1].setEnabled(true_or_false);
				} 
				if(coordinate[x][y+1].getText().equals("")) {
					coordinate[x][y+1].setEnabled(true_or_false);
				}
				
			}
		} else if(x>0 && x<limit && y==0) { 
			if(coordinate[x-1][y].getText().equals("")) {
				coordinate[x-1][y].setEnabled(true_or_false);
			} 
			if(coordinate[x+1][y].getText().equals("")) {
				coordinate[x+1][y].setEnabled(true_or_false);
			} 
			if(coordinate[x][y+1].getText().equals("")) {
				coordinate[x][y+1].setEnabled(true_or_false);
			}
		} else if(x > 0 && x < limit && y == limit) { 
			if(coordinate[x-1][y].getText().equals("")) {
				coordinate[x-1][y].setEnabled(true_or_false);
			} 
			if(coordinate[x+1][y].getText().equals("")) {
				coordinate[x+1][y].setEnabled(true_or_false);
			} 
			if(coordinate[x][y-1].getText().equals("")) {
				coordinate[x][y-1].setEnabled(true_or_false);
			}
		}
	}
	
}
