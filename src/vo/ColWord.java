package vo;

import java.io.Serializable;
import java.util.ArrayList;

public class ColWord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private ArrayList<JButton> word;
	private ArrayList<int[]> col_wordXYList;
	private ArrayList<String> wordnameList;
	private ArrayList<String> NorC_CombinationList;

	public ColWord() {
	}

	// public ColWord(ArrayList<JButton> word, ArrayList<String>
	// NorC_CombinationList) {
	// this.word = word;
	// this.NorC_CombinationList = NorC_CombinationList;
	// }

	public ColWord(ArrayList<int[]> col_wordXYList, ArrayList<String> wordnameList,
			ArrayList<String> NorC_CombinationList) {
		this.col_wordXYList = col_wordXYList;
		this.wordnameList = wordnameList;
		this.NorC_CombinationList = NorC_CombinationList;
	}

	public boolean check_ColGridStatus() {
		boolean result = false;
		for (String str : NorC_CombinationList) {
			System.out.println(str);
			if (str.equals(MyConstants.GRID_STATUS_NEW)) {
				result = true;
			}
		}
		return result;
	}

	public String showWord() {
		StringBuffer strBuffer = new StringBuffer();
		for (String str : wordnameList) {
			strBuffer.append(str);
		}
		strBuffer.trimToSize();
		return strBuffer.toString();
	}

	public int getWordSize() {
		return col_wordXYList.size();
	}

	// public void addBtn(JButton btn) {
	// word.add(btn);
	// }

	public void addBtn(int[] xy) {
		col_wordXYList.add(xy);
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

	public ArrayList<int[]> getCol_wordXYList() {
		return col_wordXYList;
	}

	public void setCol_wordXYList(ArrayList<int[]> col_wordXYList) {
		this.col_wordXYList = col_wordXYList;
	}

	public ArrayList<String> getWordnameList() {
		return wordnameList;
	}

	public void setWordnameList(ArrayList<String> wordnameList) {
		this.wordnameList = wordnameList;
	}
}
