package vo;

import java.io.Serializable;
import java.util.ArrayList;

public class RowWord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private ArrayList<JButton> word;
	private ArrayList<int[]> row_wordXYList;
	private ArrayList<String> wordnameList;
	private ArrayList<String> NorC_CombinationList;

	public RowWord() {
	}

	// public RowWord(ArrayList<JButton> word, ArrayList<String>
	// NorC_CombinationList) {
	// this.word = word;
	// this.NorC_CombinationList = NorC_CombinationList;
	// }

	public RowWord(ArrayList<int[]> row_wordXYList, ArrayList<String> wordnameList,
			ArrayList<String> NorC_CombinationList) {
		this.row_wordXYList = row_wordXYList;
		this.wordnameList = wordnameList;
		this.NorC_CombinationList = NorC_CombinationList;
	}

	public String showWord() {
		StringBuffer strBuffer = new StringBuffer();
		for (String str : wordnameList) {
			strBuffer.append(str);
		}
		strBuffer.trimToSize();
		return strBuffer.toString();
	}

	// public int getWordSize() {
	// return word.size();
	// }

	// public void addBtn(JButton btn) {
	// word.add(btn);
	// }

	public int getWordSize() {
		return row_wordXYList.size();
	}

	public void addBtn(int[] xy) {
		row_wordXYList.add(xy);
	}

	public void addWordNameList(String str) {
		wordnameList.add(str);
	}

	public void addNorC_CombinationList(String constant) {
		NorC_CombinationList.add(constant);
	}

	// public ArrayList<JButton> getWord() {
	// return word;
	// }

	// public void setWord(ArrayList<JButton> word) {
	// this.word = word;
	// }

	public ArrayList<String> getNorC_CombinationList() {
		return NorC_CombinationList;
	}

	public void setNorC_CombinationList(ArrayList<String> norC_CombinationList) {
		NorC_CombinationList = norC_CombinationList;
	}

	public ArrayList<int[]> getRow_wordXYList() {
		return row_wordXYList;
	}

	public void setRow_wordXYList(ArrayList<int[]> row_wordXYList) {
		this.row_wordXYList = row_wordXYList;
	}

	public ArrayList<String> getWordnameList() {
		return wordnameList;
	}

	public void setWordnameList(ArrayList<String> wordnameList) {
		this.wordnameList = wordnameList;
	}
}
