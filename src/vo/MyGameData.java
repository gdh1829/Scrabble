package vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MyGameData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/////////////////////// Ŭ���̾�Ʈ�� ���� �� ����ϴ� ��� ////////////////
	private Word word;
	private ArrayList<int[]> selectedBtnXYListByMousePressed;
	private HashMap<int[], String> alphabetListbyMousePressed;
	//////////////////////// ������ ���� �� ����ϴ� ��� ////////////
	private ArrayList<int[]> row_resultXY;
	private ArrayList<int[]> col_resultXY;
	private String currentUserId;
	private int plusScore;
	private ArrayList<String> answerCollection;
	private HashMap<int[], String> row_stringCollectionMap;
	private HashMap<int[], String> col_stringCollectionMap;

	public MyGameData(Word word, ArrayList<int[]> selectedBtnXYListByMousePressed,
			HashMap<int[], String> alphabetListbyMousePressed) { // Ŭ���̾�Ʈ�� ������
		this.word = word;
		this.selectedBtnXYListByMousePressed = selectedBtnXYListByMousePressed;
		this.alphabetListbyMousePressed = alphabetListbyMousePressed;
	}

	// Ŭ���̾�Ʈ�� ������
	public MyGameData(ArrayList<int[]> row_resultXY, ArrayList<int[]> col_resultXY, String currentUserId, int plusScore,
			ArrayList<String> answerCollection, HashMap<int[], String> row_stringCollectionMap,
			HashMap<int[], String> col_stringCollectionMap) {
		this.row_resultXY = row_resultXY;
		this.col_resultXY = col_resultXY;
		this.currentUserId = currentUserId;
		this.plusScore = plusScore;
		this.answerCollection = answerCollection;
		this.row_stringCollectionMap = row_stringCollectionMap;
		this.col_stringCollectionMap = col_stringCollectionMap;

	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	public ArrayList<int[]> getSelectedBtnXYListByMousePressed() {
		return selectedBtnXYListByMousePressed;
	}

	public void setSelectedBtnXYListByMousePressed(ArrayList<int[]> selectedBtnXYListByMousePressed) {
		this.selectedBtnXYListByMousePressed = selectedBtnXYListByMousePressed;
	}

	public ArrayList<int[]> getRow_resultXY() {
		return row_resultXY;
	}

	public void setRow_resultXY(ArrayList<int[]> row_resultXY) {
		this.row_resultXY = row_resultXY;
	}

	public ArrayList<int[]> getCol_resultXY() {
		return col_resultXY;
	}

	public void setCol_resultXY(ArrayList<int[]> col_resultXY) {
		this.col_resultXY = col_resultXY;
	}

	public int getPlusScore() {
		return plusScore;
	}

	public void setPlusScore(int plusScore) {
		this.plusScore = plusScore;
	}

	public ArrayList<String> getAnswerCollection() {
		return answerCollection;
	}

	public void setAnswerCollection(ArrayList<String> answerCollection) {
		this.answerCollection = answerCollection;
	}

	public HashMap<int[], String> getAlphabetListbyMousePressed() {
		return alphabetListbyMousePressed;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public HashMap<int[], String> getRow_stringCollectionMap() {
		return row_stringCollectionMap;
	}

	public void setRow_stringCollectionMap(HashMap<int[], String> row_stringCollectionMap) {
		this.row_stringCollectionMap = row_stringCollectionMap;
	}

	public HashMap<int[], String> getCol_stringCollectionMap() {
		return col_stringCollectionMap;
	}

	public void setCol_stringCollectionMap(HashMap<int[], String> col_stringCollectionMap) {
		this.col_stringCollectionMap = col_stringCollectionMap;
	}

}
