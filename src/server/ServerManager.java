package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import vo.ColWord;
import vo.MyConstants;
import vo.RowWord;

public class ServerManager {

	Connection con;
	String DBfoundWord;
	ArrayList<String> answerCollection;
	HashMap<int[], String> row_stringCollectionMap;
	HashMap<int[], String> col_stringCollectionMap;

	public ServerManager() {
		answerCollection = new ArrayList<>();
		row_stringCollectionMap = new HashMap<>();
		col_stringCollectionMap = new HashMap<>();
	}

	public boolean addClient(String id, String pw) {
		con = ConnectionManager.getConnection();
		PreparedStatement pstmt;

		try {
			pstmt = con.prepareStatement("insert into user_db(id, pw) values(?, ?)");
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.executeQuery();

			return true;

		} catch (SQLException e) {
			return false;
		}
	}

	public int checkClient(String id, String input_pw) {
		con = ConnectionManager.getConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		String pw = null;

		try {
			pstmt = con.prepareStatement("select pw from user_db where id = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				pw = rs.getString("pw");
			}

			if (pw == null) {
				return 0;
			} else if (!pw.equals(input_pw)) {
				return 1;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return 3;
	}

	public boolean findWord(String word) {
		boolean result = false;
		con = ConnectionManager.getConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		String found_word = null;

		try {
			pstmt = con.prepareStatement("select word from word_db where word = ?");
			pstmt.setString(1, word);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				found_word = rs.getString(1);
			}

			if (found_word != null && found_word.equals(word)) {
				result = true;
				DBfoundWord = found_word;
			}

		} catch (SQLException se) {
			se.printStackTrace();
			result = false;
		}
		return result;
	}

	public ArrayList<int[]> row_wordChecker(ArrayList<RowWord> row_wordXYList,
			ArrayList<int[]> selectedBtnXYListByMousePressed, HashMap<int[], String> alphabetListbyMousePressed) {

		ArrayList<int[]> correctAnswerXYList = new ArrayList<>();
		System.out.println("Checking if the Founded Row Words are available...");

		for (RowWord word : row_wordXYList) { 
			if (check_GridStatus(word.getNorC_CombinationList())) { //to check if it is a new word combination created with user's new inputs 
				if (this.findWord(word.showWord().toLowerCase())) { // Database search to find it out that it is an appropriate word 
					System.out.println(word.showWord());
					String str = DBfoundWord;
					answerCollection.add(str); // add the words searched in the dictionary into the list to show the answers to client
					for (int[] rowXY : word.getRow_wordXYList()) {
						correctAnswerXYList.add(rowXY);
						for (int[] mouseXY : selectedBtnXYListByMousePressed) {
							//comparing XYcoordinates of the words and ones pressed by user to find out the common coordinate
							//to alter the color the area that the user selected with the user's color in client's views later
							if (rowXY[0] == mouseXY[0] && rowXY[1] == mouseXY[1]) {
								System.out.println("confirmed");
								String correctStr = alphabetListbyMousePressed.get(mouseXY);
								row_stringCollectionMap.put(rowXY, correctStr);
							}

						}
					}
				}
			}
		}
		return correctAnswerXYList;
	}

	public ArrayList<int[]> col_wordChecker(ArrayList<ColWord> col_wordList,
			ArrayList<int[]> selectedBtnXYListByMousePressed, HashMap<int[], String> alphabetListbyMousePressed) {

		ArrayList<int[]> correctAnswerXYList = new ArrayList<>();
		System.out.println("Checking if the Founded Col Words are available...");

		for (ColWord word : col_wordList) {
			if (check_GridStatus(word.getNorC_CombinationList())) { //to check if it is a new word combination created with user's new inputs
				if (this.findWord(word.showWord().toLowerCase())) {  // Database search to find it out that it is an appropriate word 
					System.out.println(word.showWord());
					String str = DBfoundWord;
					answerCollection.add(str); // add the words searched in the dictionary into the list to show the answers to client
					for (int[] colXY : word.getCol_wordXYList()) {
						correctAnswerXYList.add(colXY);
						for (int[] mouseXY : selectedBtnXYListByMousePressed) {
							//comparing XYcoordinates of the words and ones pressed by user to find out the common coordinate
							//to alter the color the area that the user selected with the user's color in client's views later
							if (colXY[0] == mouseXY[0] && colXY[1] == mouseXY[1]) {
								System.out.println("confirmed");
								String correctStr = alphabetListbyMousePressed.get(mouseXY);
								col_stringCollectionMap.put(colXY, correctStr);
							}
						}
					}
				}
			}
		}
		return correctAnswerXYList;
	}

	public boolean check_GridStatus(ArrayList<String> NorC_CombinationList) {
		boolean result = false;
		for (String str : NorC_CombinationList) {
			System.out.println(str);
			if (str.equals(MyConstants.GRID_STATUS_NEW)) {
				result = true;
			}
		}
		return result;
	}

	public ArrayList<String> getAnswerCollection() {
		return answerCollection;
	}

	public void initAnswerCollection() {
		answerCollection.clear();
	}

	public HashMap<int[], String> getRow_stringCollectionMap() {
		return row_stringCollectionMap;
	}

	public void initRow_stringCollectionMap() {
		this.row_stringCollectionMap.clear();
	}

	public HashMap<int[], String> getCol_stringCollectionMap() {
		return col_stringCollectionMap;
	}

	public void initCol_stringCollectionMap() {
		this.col_stringCollectionMap.clear();
		;
	}
}
