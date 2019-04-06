package vo;

import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<RowWord> rowwords;
	private ArrayList<ColWord> colwords;

	public Word(ArrayList<RowWord> rowwords, ArrayList<ColWord> colwords) {
		this.rowwords = rowwords;
		this.colwords = colwords;
	}

	public ArrayList<RowWord> getRowwords() {
		return rowwords;
	}

	public void setRowwords(ArrayList<RowWord> rowwords) {
		this.rowwords = rowwords;
	}

	public ArrayList<ColWord> getColwords() {
		return colwords;
	}

	public void setColwords(ArrayList<ColWord> colwords) {
		this.colwords = colwords;
	}

}
